package com.lee.crowdtracker.root

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.crowdtracker.core.domain.beach.usecase.area.DownloadAreaByCsvUseCase
import com.lee.crowdtracker.library.base.exts.runSuspendCatching
import com.lee.crowdtracker.libray.navermap.NaverMapSdkControllerImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainViewModel"

@HiltViewModel
class MainViewModel @Inject constructor(
    private val downloadAreaByCsvUseCase: DownloadAreaByCsvUseCase,
    private val naverMapSdkControllerImpl: NaverMapSdkControllerImpl,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val isPermissionGranted = savedStateHandle.getStateFlow(PERMISSION_GRANTED, false)
    private val isDownloadArea = savedStateHandle.getStateFlow(IS_DOWNLOAD_AREA, false)
    private val isInitMap = savedStateHandle.getStateFlow(INIT_MAP, false)

    val isInitialized: StateFlow<Boolean> = combine(
        isPermissionGranted,
        isDownloadArea,
        isInitMap
    ) { isPermissionGranted, isDownloadArea, isInitMap ->
        isPermissionGranted && isDownloadArea && isInitMap
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        false
    )

    fun initNaverMapSdk() {
        naverMapSdkControllerImpl.init()
        savedStateHandle[INIT_MAP] = true
    }

    fun onPermissionGranted() {
        savedStateHandle[PERMISSION_GRANTED] = true
    }

    fun downloadArea() {
        viewModelScope.launch {
            runSuspendCatching {
                downloadAreaByCsvUseCase()
            }.onSuccess {
                Log.d("TAG", "downloadArea: success")
                savedStateHandle[IS_DOWNLOAD_AREA] = true
            }.onFailure {
                Log.d("TAG", "downloadArea: fail", it)
            }
        }
    }

    companion object {
        private const val PERMISSION_GRANTED = "permissionGranted"
        private const val INIT_MAP = "initMap"
        private const val IS_DOWNLOAD_AREA = "isDownloadArea"
    }
}