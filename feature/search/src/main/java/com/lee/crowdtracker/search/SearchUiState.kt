package com.lee.crowdtracker.search

import com.lee.crowdtracker.core.domain.beach.model.Area

sealed interface SearchUiState {
    data object Loading : SearchUiState

    data object Empty : SearchUiState

    data class Success(
        val areaList: List<Area>
    ) : SearchUiState

    data class Error(
        val message: String
    ) : SearchUiState
}