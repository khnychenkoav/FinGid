package com.example.fingid.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import com.example.fingid.R
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.fingid.navigation.Screen

sealed class BottomNavItem(
    val route: String,
    val titleResId: Int,
    val icon: ImageVector
) {
    object Expenses : BottomNavItem(Screen.Expenses.route, R.string.bottom_nav_expenses, Icons.Filled.Payment)
    object Income : BottomNavItem(Screen.Income.route, R.string.bottom_nav_income, Icons.Filled.AccountBalanceWallet)
    object Account : BottomNavItem(Screen.Account.route, R.string.bottom_nav_account, Icons.Filled.Analytics)
    object Articles : BottomNavItem(Screen.Articles.route, R.string.bottom_nav_articles,
        Icons.AutoMirrored.Filled.ListAlt
    )
    object Settings : BottomNavItem(Screen.Settings.route, R.string.bottom_nav_settings, Icons.Filled.Settings)
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

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = stringResource(item.titleResId)) },
                label = { Text(stringResource(item.titleResId)) },
                selected = currentRoute == item.route,
                alwaysShowLabel = true,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}