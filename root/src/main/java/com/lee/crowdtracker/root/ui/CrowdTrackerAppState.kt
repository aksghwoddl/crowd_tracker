package com.lee.crowdtracker.root.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lee.crowdtracker.core.presenter.screen.CrowdTrackerScreen
import com.lee.crowdtracker.libray.design.navigation.TopLevelDestination

@Composable
fun rememberCrowdTrackerAppState(
    navController: NavHostController = rememberNavController(),
    snackbarHostState: SnackbarHostState
): CrowdTrackerAppState {
    return CrowdTrackerAppState(
        navController = navController,
        snackbarHostState = snackbarHostState
    )
}

@Stable
data class CrowdTrackerAppState(
    val navController: NavHostController,
    val snackbarHostState: SnackbarHostState
) {
    private var currentDestination: TopLevelDestination? = null

    fun navTopLevelDestination(destination: TopLevelDestination) {
        currentDestination?.let {
            if (it == destination) return
        }
        val route = when (destination) {
            is TopLevelDestination.Home -> CrowdTrackerScreen.HomeRoute.route
            is TopLevelDestination.Search -> CrowdTrackerScreen.SearchRoute.route
        }

        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        currentDestination = destination
    }
}