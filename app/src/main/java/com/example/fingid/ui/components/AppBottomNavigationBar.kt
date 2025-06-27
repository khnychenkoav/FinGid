package com.example.fingid.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.fingid.ui.commonitems.BottomNavigationItem

@Composable
fun AppBottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavigationItem.Expenses,
        BottomNavigationItem.Income,
        BottomNavigationItem.Account,
        BottomNavigationItem.Articles,
        BottomNavigationItem.Settings
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        items.forEach { item ->
            val selected = navBackStackEntry?.destination?.hierarchy?.any {
                it.route == item.screen.route
            } ?: false

            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(painterResource(item.iconResId), contentDescription = null)
                },
                label = {
                    Text(text = stringResource(item.titleResId))
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}