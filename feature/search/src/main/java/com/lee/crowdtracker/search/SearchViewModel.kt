package com.lee.crowdtracker.search

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.lee.crowdtracker.core.domain.beach.usecase.GetAreaListByNameUseCase
import com.lee.crowdtracker.core.presenter.base.BaseViewModel
import com.lee.crowdtracker.library.base.exts.runSuspendCatching
import com.lee.crowdtracker.search.model.SearchScreenEvent
import com.lee.crowdtracker.search.model.SearchScreenSideEffect
import com.lee.crowdtracker.search.model.SearchScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getAreaListByNameUseCase: GetAreaListByNameUseCase,
) : BaseViewModel<SearchScreenState, SearchScreenEvent>(
    initialState = SearchScreenState()
) {
    override fun handleEvent(event: SearchScreenEvent) {
        when (event) {
            is SearchScreenEvent.OnChangeSearchText -> {
                updateState {
                    it.copy(
                        searchText = event.text
                    )
                }
            }

            SearchScreenEvent.OnSearchButtonClick -> {
                handleSideEffect(sideEffect = SearchScreenSideEffect.SearchArea(text = state.value.searchText))
            }

            is SearchScreenEvent.OnSearchAreaSuccess -> {
                Log.d("TAG", "OnSearchAreaSuccess: $event")
                updateState {
                    it.copy(
                        areaList = event.areaList
                    )
                }
            }
        }
    }

    private fun handleSideEffect(sideEffect: SearchScreenSideEffect) {
        when (sideEffect) {
            is SearchScreenSideEffect.SearchArea -> {
                searchAreaListByName(text = sideEffect.text)
            }
        }
    }

    private fun searchAreaListByName(text: String) {
        viewModelScope.launch {
            runSuspendCatching {
                getAreaListByNameUseCase(name = text)
            }.onSuccess {
                handleEvent(SearchScreenEvent.OnSearchAreaSuccess(areaList = it.toPersistentList()))
            }.onFailure {
                Log.d("TAG", "handleSideEffect: ")
            }
        }
    }
}