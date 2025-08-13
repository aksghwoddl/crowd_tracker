package com.lee.crowdtracker.search

import com.lee.crowdtracker.core.domain.beach.model.AreaModel

sealed interface SearchUiState {
    data object Loading : SearchUiState

    data object Empty : SearchUiState

    data class Success(
        val areaModelList: List<AreaModel>
    ) : SearchUiState

    data class Error(
        val message: String
    ) : SearchUiState
}