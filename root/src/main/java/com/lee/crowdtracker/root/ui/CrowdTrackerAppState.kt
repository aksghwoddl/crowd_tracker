package com.lee.crowdtracker.root.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lee.crowdtracker.core.presenter.screen.CrowdTrackerScreen
import com.lee.crowdtracker.libray.design.navigation.TopLevelDestination

@Composable
fun rememberCrowdTrackerAppState(
    navController: NavHostController = rememberNavController()
): CrowdTrackerAppState {
    return CrowdTrackerAppState(navController = navController)
}

@Stable
data class CrowdTrackerAppState(
    val navController: NavHostController
) {
    fun navTopLevelDestination(destination: TopLevelDestination) {
        when (destination) {
            is TopLevelDestination.Home -> {
                navController.navigate(CrowdTrackerScreen.HomeRoute.route)
            }

            is TopLevelDestination.Search -> {
                navController.navigate(CrowdTrackerScreen.SearchRoute.route)
            }
        }
    }
}