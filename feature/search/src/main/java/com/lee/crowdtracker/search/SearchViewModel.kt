package com.lee.crowdtracker.search

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.crowdtracker.core.domain.beach.usecase.area.GetAreaListByNameUseCase
import com.lee.crowdtracker.core.domain.beach.usecase.citydata.GetCityDataUseCase
import com.lee.crowdtracker.search.model.Area
import com.lee.crowdtracker.search.model.toArea
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val TAG = "SearchViewModel"

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getAreaListByNameUseCase: GetAreaListByNameUseCase,
    private val getCityDataUseCase: GetCityDataUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val searchQuery = savedStateHandle.getStateFlow(key = SEARCH_QUERY, initialValue = "")
    private val selectedAreaName =
        savedStateHandle.getStateFlow(key = SELECTED_AREA_NAME, initialValue = "")

    val isShowDialog = savedStateHandle.getStateFlow(key = IS_SHOW_DIALOG, initialValue = false)

    fun onQueryChange(query: String) {
        savedStateHandle[SEARCH_QUERY] = query
    }

    fun onAreaClick(area: Area) {
        savedStateHandle[SELECTED_AREA_NAME] = area.name
        savedStateHandle[IS_SHOW_DIALOG] = true
    }

    fun onCityContentsDialogDismiss() {
        savedStateHandle[SELECTED_AREA_NAME] = ""
        savedStateHandle[IS_SHOW_DIALOG] = false
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchUiState: StateFlow<SearchUiState> = searchQuery
        .debounce(300)
        .filter { it.isNotBlank() }
        .distinctUntilChanged()
        .flatMapLatest {
            getAreaListByNameUseCase(name = it).map { areaList ->
                if (areaList.isEmpty()) {
                    SearchUiState.Empty
                } else {
                    SearchUiState.Success(
                        areaList = areaList.map { areaModel ->
                            areaModel.toArea()
                        }
                    )
                }
            }.catch { throwable ->
                Log.e(TAG, throwable.message.toString())
                emit(SearchUiState.Error("문제가 발생 했습니다."))
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SearchUiState.Loading
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val cityDataUiState: StateFlow<CityDataUiState> = selectedAreaName
        .flatMapLatest {
            if (it.isEmpty()) { // 값이 비어 있다면 초기 상태
                flowOf(CityDataUiState.Loading)
            } else {
                getCityDataUseCase(name = it).map { cityDataList ->
                    cityDataList.firstOrNull()?.run {
                        CityDataUiState.Success(
                            name = name,
                            level = congestionLevel,
                            message = congestionMessage
                        )
                    } ?: CityDataUiState.Loading
                }.catch { throwable ->
                    Log.e(TAG, throwable.message.toString())
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CityDataUiState.Loading
        )
}

private const val SEARCH_QUERY = "searchQuery"
private const val SELECTED_AREA_NAME = "selectedAreaName"
private const val IS_SHOW_DIALOG = "isShowDialog"