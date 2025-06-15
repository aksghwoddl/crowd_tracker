package com.lee.crowdtracker.root

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.crowdtracker.core.domain.beach.usecase.DownloadAreaByCsvUseCase
import com.lee.crowdtracker.core.domain.beach.usecase.GetAreaListUseCase
import com.lee.crowdtracker.library.base.exts.runSuspendCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val downloadAreaByCsvUseCase: DownloadAreaByCsvUseCase,
    private val getAreaListUseCase: GetAreaListUseCase,
) : ViewModel() {
    fun downloadArea() {
        viewModelScope.launch {
            runSuspendCatching {
                downloadAreaByCsvUseCase()
            }.onSuccess {
                Log.d("TAG", "downloadArea: success")
                getAreaList()
            }.onFailure {
                Log.d("TAG", "downloadArea: fail", it)
            }
        }
    }

    suspend fun getAreaList() {
        runSuspendCatching {
            getAreaListUseCase()
        }.onSuccess {
            Log.d("TAG", "getAreaList: success => $it")
        }.onFailure {
            Log.d("TAG", "getAreaList: fail", it)
        }
    }
}