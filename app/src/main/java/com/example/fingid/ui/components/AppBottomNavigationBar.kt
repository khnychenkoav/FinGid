package com.example.fingid.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.fingid.R
import com.example.fingid.navigation.Screen

sealed class BottomNavItem(
    val screen: Screen,
    val titleResId: Int,
    val icon: ImageVector
) {
    object Expenses : BottomNavItem(Screen.Expenses, R.string.bottom_nav_expenses, Icons.Filled.Payment)
    object Income   : BottomNavItem(Screen.Income,   R.string.bottom_nav_income,   Icons.Filled.AccountBalanceWallet)
    object Account  : BottomNavItem(Screen.Account,  R.string.bottom_nav_account,  Icons.Filled.Analytics)
    object Articles : BottomNavItem(Screen.Articles, R.string.bottom_nav_articles, Icons.Filled.Analytics)
    object Settings : BottomNavItem(Screen.Settings, R.string.bottom_nav_settings, Icons.Filled.Settings)
}


@Composable
fun AppBottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Expenses,
        BottomNavItem.Income,
        BottomNavItem.Account,
        BottomNavItem.Articles,
        BottomNavItem.Settings
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.screen.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = stringResource(item.titleResId)) },
                label = { Text(stringResource(item.titleResId)) },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}