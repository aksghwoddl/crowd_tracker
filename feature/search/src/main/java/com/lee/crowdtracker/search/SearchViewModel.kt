package com.lee.crowdtracker.search

import androidx.lifecycle.viewModelScope
import com.lee.crowdtracker.core.domain.beach.usecase.GetAreaListByNameUseCase
import com.lee.crowdtracker.core.presenter.base.BaseViewModel
import com.lee.crowdtracker.library.base.exts.runSuspendCatching
import com.lee.crowdtracker.search.model.SearchScreenEffect
import com.lee.crowdtracker.search.model.SearchScreenEvent
import com.lee.crowdtracker.search.model.SearchScreenSideEffect
import com.lee.crowdtracker.search.model.SearchScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getAreaListByNameUseCase: GetAreaListByNameUseCase,
) : BaseViewModel<SearchScreenState, SearchScreenEvent>(
    initialState = SearchScreenState()
) {
    private val _effect: MutableSharedFlow<SearchScreenEffect> = MutableSharedFlow()
    val effect: SharedFlow<SearchScreenEffect> = _effect.asSharedFlow()

    private var textChangeDebounceJob: Job? = null

    override fun handleEvent(event: SearchScreenEvent) {
        when (event) {
            is SearchScreenEvent.OnChangeSearchText -> {
                updateState {
                    it.copy(
                        searchText = event.text
                    )
                }
                textChangeDebounceJob?.cancel()
                textChangeDebounceJob = viewModelScope.launch {
                    delay(500)
                    handleSideEffect(sideEffect = SearchScreenSideEffect.SearchArea(text = state.value.searchText))
                }
            }

            is SearchScreenEvent.OnSearchAreaSuccess -> {
                updateState {
                    it.copy(
                        areaList = event.areaList
                    )
                }
            }

            SearchScreenEvent.OnKeyboardActionSearch -> {
                handleSideEffect(sideEffect = SearchScreenSideEffect.SearchArea(text = state.value.searchText))
            }

            is SearchScreenEvent.OnClickArea -> {
                emitEffect(
                    SearchScreenEffect.ShowSnackBar(
                        message = "${event.area.name} 클릭",
                        actionLabel = null
                    )
                )
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

    private fun emitEffect(effect: SearchScreenEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

    private fun searchAreaListByName(text: String) {
        viewModelScope.launch {
            runSuspendCatching {
                getAreaListByNameUseCase(name = text)
            }.onSuccess {
                handleEvent(SearchScreenEvent.OnSearchAreaSuccess(areaList = it.toPersistentList()))
            }.onFailure {
                emitEffect(
                    SearchScreenEffect.ShowSnackBar(
                        message = "문제가 발생 했습니다.",
                        actionLabel = null,
                    )
                )
            }
        }
    }
}