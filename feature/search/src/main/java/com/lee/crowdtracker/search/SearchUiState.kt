package com.lee.crowdtracker.search

import androidx.compose.runtime.Stable
import com.lee.crowdtracker.search.model.Area

@Stable
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