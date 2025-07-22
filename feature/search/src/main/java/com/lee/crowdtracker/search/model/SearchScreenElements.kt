package com.lee.crowdtracker.search.model

import androidx.compose.runtime.Immutable
import com.lee.crowdtracker.core.domain.beach.model.Area
import com.lee.crowdtracker.core.presenter.base.BaseEvent
import com.lee.crowdtracker.core.presenter.base.BaseState
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class SearchScreenState(
    val searchText: String = "",
    val areaList: PersistentList<Area> = persistentListOf()
) : BaseState

sealed interface SearchScreenEvent : BaseEvent {
    data class OnChangeSearchText(val text: String) : SearchScreenEvent
    data class OnSearchAreaSuccess(val areaList: PersistentList<Area>) : SearchScreenEvent
    data object OnKeyboardActionSearch : SearchScreenEvent
    data class OnClickArea(val area: Area) : SearchScreenEvent
}

@Immutable
sealed interface SearchScreenEffect {
    data class ShowSnackBar(
        val message: String,
        val actionLabel: String?,
    ) : SearchScreenEffect
}

@Immutable
sealed interface SearchScreenSideEffect {
    data class SearchArea(val text: String) : SearchScreenSideEffect
}