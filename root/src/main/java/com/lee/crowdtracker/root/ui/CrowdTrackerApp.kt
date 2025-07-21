package com.lee.crowdtracker.root.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lee.crowdtracker.core.presenter.screen.CrowdTrackerScreen
import com.lee.crowdtracker.libray.design.component.BottomNavigationBar
import com.lee.crowdtracker.libray.design.navigation.TopLevelDestination
import com.lee.crowdtracker.root.Greeting
import com.lee.crowdtracker.search.SearchRoute

@Composable
internal fun CrowdTrackerApp(
    appState: CrowdTrackerAppState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigationBar(
                navItems = TopLevelDestination.items,
                onNavItem = { destination ->
                    appState.navTopLevelDestination(destination = destination)
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = appState.navController,
            modifier = Modifier.padding(innerPadding),
            startDestination = CrowdTrackerScreen.HomeRoute.route,
        ) {
            composable(CrowdTrackerScreen.SearchRoute.route) {
                SearchRoute()
            }
            composable(CrowdTrackerScreen.HomeRoute.route) {
                Greeting(name = CrowdTrackerScreen.HomeRoute.route)
            }
        }
    }

}