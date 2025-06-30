package com.lee.crowdtracker.core.presenter.screen

import kotlinx.serialization.Serializable

@Serializable
sealed class CrowdTrackerScreen(
    val route: String,
) {
    @Serializable
    data object SearchRoute : CrowdTrackerScreen(route = "search")

    @Serializable
    data object HomeRoute : CrowdTrackerScreen(route = "home")
}