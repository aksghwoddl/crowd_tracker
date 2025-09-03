package com.lee.crowdtracker.search

import androidx.compose.runtime.Stable
import com.lee.crowdtracker.core.domain.beach.model.CongestionLevel

@Stable
sealed interface CityDataUiState {
    data object Loading : CityDataUiState

    data class Success(
        val name: String,
        val level: CongestionLevel,
        val message: String,
    ) : CityDataUiState
}