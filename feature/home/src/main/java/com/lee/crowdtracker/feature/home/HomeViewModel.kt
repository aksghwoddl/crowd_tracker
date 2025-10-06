package com.lee.crowdtracker.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.crowdtracker.core.domain.beach.usecase.area.GetAreaListUseCase
import com.lee.crowdtracker.core.domain.beach.usecase.citydata.GetCityDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext

private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAreaListUseCase: GetAreaListUseCase,
    private val getCityDataUseCase: GetCityDataUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadMarkers()
    }

    fun refresh() {
        loadMarkers()
    }

    private fun loadMarkers() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.emit(HomeUiState.Loading)
            try {
                val areas = withContext(Dispatchers.IO) {
                    getAreaListUseCase()
                }

                if (areas.isEmpty()) {
                    _uiState.emit(HomeUiState.Error("저장된 지역 정보가 없습니다."))
                    return@launch
                }

                val semaphore = Semaphore(permits = 6)

                val markers = withContext(Dispatchers.IO) {
                    areas.map { area ->
                        async {
                            semaphore.withPermit {
                                runCatching {
                                    getCityDataUseCase(name = area.name).first()
                                        .firstOrNull()
                                }.onFailure { error ->
                                    Log.e(TAG, "도시 데이터 요청 실패(${area.name})", error)
                                }.getOrNull()?.let { cityData ->
                                    AreaCongestionUiModel(
                                        id = area.no,
                                        name = cityData.name,
                                        category = area.category,
                                        congestionLevel = cityData.congestionLevel,
                                        congestionMessage = cityData.congestionMessage,
                                    )
                                }
                            }
                        }
                    }.mapNotNull { it.await() }
                        .sortedBy { it.id }
                }

                if (markers.isEmpty()) {
                    _uiState.emit(HomeUiState.Error("혼잡도 정보를 불러오지 못했습니다."))
                } else {
                    _uiState.emit(HomeUiState.Success(markers = markers))
                }
            } catch (cancellation: CancellationException) {
                throw cancellation
            } catch (throwable: Throwable) {
                Log.e(TAG, "혼잡도 로딩 실패", throwable)
                _uiState.emit(HomeUiState.Error("혼잡도 정보를 불러오지 못했습니다."))
            }
        }
    }
}
