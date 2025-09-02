package com.lee.crowdtracker.search

import androidx.compose.runtime.Stable

@Stable
sealed interface CityDataUiState {
    data object Loading : CityDataUiState

    data class Success(
        val name: String,
        val level: String,
        val message: String,
    ) : CityDataUiState
}