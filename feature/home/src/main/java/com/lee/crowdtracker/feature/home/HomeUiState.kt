package com.lee.crowdtracker.feature.home

import androidx.compose.runtime.Stable
import com.lee.crowdtracker.core.domain.beach.model.CongestionLevel

@Stable
sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        val markers: List<AreaCongestionUiModel>,
    ) : HomeUiState

    data class Error(
        val message: String,
    ) : HomeUiState
}

@Stable
data class AreaCongestionUiModel(
    val id: Int,
    val name: String,
    val category: String,
    val congestionLevel: CongestionLevel,
    val congestionMessage: String,
)
