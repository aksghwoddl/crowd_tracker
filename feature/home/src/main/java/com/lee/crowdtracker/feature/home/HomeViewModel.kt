package com.lee.crowdtracker.feature.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.crowdtracker.core.domain.beach.model.CrowdDataModel
import com.lee.crowdtracker.core.domain.beach.usecase.home.GetCrowdDataUseCase
import com.lee.crowdtracker.libray.navermap.NaverMapSdkController
import com.lee.crowdtracker.libray.navermap.model.CrowdMarkerData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCrowdDataUseCase: GetCrowdDataUseCase,
    private val naverMapSdkController: NaverMapSdkController,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val fetchTrigger = MutableSharedFlow<Unit>()

    val selectedMarkerId = savedStateHandle.getStateFlow(KEY_SELECTED_MARKER_ID, "")

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<HomeUiState> = fetchTrigger
        .onStart { emit(Unit) }
        .flatMapLatest {
            flow {
                emit(HomeUiState.Loading)
                val crowdDataList = getCrowdDataUseCase()

                if (crowdDataList.isEmpty()) {
                    emit(HomeUiState.Error("마커정보가 비어있습니다."))
                } else {
                    emit(
                        HomeUiState.Success(
                            crowdMarkerData = buildCrowdMarkerDataList(
                                crowdDataList = crowdDataList
                            ).toPersistentList()
                        )
                    )
                }
            }.catch {
                emit(HomeUiState.Error("마커 정보를 불러오는중 에러 발생"))
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState.Loading
        )

    fun onRetry() {
        viewModelScope.launch {
            fetchTrigger.emit(Unit)
        }
    }

    fun onMarkerClick(id: String) {
        savedStateHandle[KEY_SELECTED_MARKER_ID] = id
    }

    fun onSelectedMarkerCardDismiss() {
        savedStateHandle[KEY_SELECTED_MARKER_ID] = ""
    }

    private suspend fun buildCrowdMarkerDataList(
        crowdDataList: List<CrowdDataModel>
    ): List<CrowdMarkerData> = withContext(Dispatchers.IO) {
        crowdDataList.map { crowdData ->
            async(Dispatchers.Default) {
                val latLng = naverMapSdkController.getLatLngByName(
                    name = crowdData.name,
                    onCancellation = { cause ->
                        Log.e(TAG, "buildCrowdMarkerDataList cancel by", cause)
                    }
                )
                if (latLng.isValid) {
                    CrowdMarkerData(
                        id = crowdData.id.toString(),
                        name = crowdData.name,
                        category = crowdData.category,
                        description = crowdData.congestionMessage,
                        level = crowdData.congestionLevel,
                        latLng = latLng
                    )
                } else {
                    null
                }
            }
        }.awaitAll().filterNotNull()
    }


    companion object {
        private const val KEY_SELECTED_MARKER_ID = "selectedMarkerId"
        private const val MAX_CONCURRENT_GEO_REQUESTS = 4
    }
}
