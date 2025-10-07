package com.lee.crowdtracker.feature.home

import androidx.compose.runtime.Stable
import com.lee.crowdtracker.libray.navermap.model.CrowdMarkerData
import kotlinx.collections.immutable.PersistentList

@Stable
sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val crowdMarkerData: PersistentList<CrowdMarkerData>,
    ) : HomeUiState

    data class Error(
        val message: String,
    ) : HomeUiState
}
