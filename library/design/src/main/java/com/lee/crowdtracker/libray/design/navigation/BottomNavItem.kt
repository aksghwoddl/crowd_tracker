package com.lee.crowdtracker.libray.design.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.collections.immutable.persistentListOf

sealed class TopLevelDestination(
    val label: String,
    val icon: ImageVector,
) {
    data object Home : TopLevelDestination(
        label = "홈",
        icon = Icons.Default.Home,
    )

    data object Search : TopLevelDestination(
        label = "검색",
        icon = Icons.Default.Search
    )

    companion object {
        val items = persistentListOf(Home , Search)
    }
}