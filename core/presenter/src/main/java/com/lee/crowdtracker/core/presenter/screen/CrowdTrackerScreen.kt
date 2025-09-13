package com.lee.crowdtracker.core.presenter.screen

import kotlinx.serialization.Serializable

@Serializable
sealed interface CrowdTrackerScreen {
    @Serializable
    data object SearchRoute : CrowdTrackerScreen

    @Serializable
    data object HomeRoute : CrowdTrackerScreen
}