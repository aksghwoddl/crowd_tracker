package com.lee.crowdtracker.libray.design.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lee.crowdtracker.libray.design.navigation.TopLevelDestination
import kotlinx.collections.immutable.PersistentList

@Composable
fun BottomNavigationBar(
    navItems: PersistentList<TopLevelDestination>,
    onNavItem: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        navItems.forEach {
            BottomTabItem(
                label = it.label,
                icon = it.icon,
                onClick = {
                    onNavItem(it)
                }
            )
        }
    }
}