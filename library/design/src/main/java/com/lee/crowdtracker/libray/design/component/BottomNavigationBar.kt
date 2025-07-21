package com.lee.crowdtracker.libray.design.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lee.crowdtracker.libray.design.navigation.TopLevelDestination
import com.lee.crowdtracker.libray.design.theme.CDTheme
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
                modifier = Modifier.weight(1f),
                label = it.label,
                icon = it.icon,
                onClick = {
                    onNavItem(it)
                }
            )
        }
    }
}

@Composable
@Preview
fun BottomTabNavigationBarPreview() {
    CDTheme {
        BottomNavigationBar(
            modifier = Modifier.fillMaxWidth(),
            navItems = TopLevelDestination.items,
            onNavItem = {}
        )
    }
}