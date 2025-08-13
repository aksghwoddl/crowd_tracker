package com.lee.crowdtracker.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.crowdtracker.core.domain.beach.usecase.area.GetAreaListByNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

private const val TAG = "SearchViewModel"

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getAreaListByNameUseCase: GetAreaListByNameUseCase,
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")

    fun onQueryChange(query: String) {
        _searchQuery.update { query }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchUiState: StateFlow<SearchUiState> = _searchQuery
        .debounce(300)
        .filter { it.isNotBlank() }
        .distinctUntilChanged()
        .flatMapLatest {
            getAreaListByNameUseCase(name = it).map { areaList ->
                Log.d(TAG ,"결과값은 $areaList")
                if (areaList.isEmpty()) {
                    SearchUiState.Empty
                } else {
                    SearchUiState.Success(areaModelList = areaList)
                }
            }.catch { throwable ->
                Log.e(TAG, throwable.message.toString())
                emit(SearchUiState.Error("문제가 발생 했습니다."))
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = SearchUiState.Loading
        )
}