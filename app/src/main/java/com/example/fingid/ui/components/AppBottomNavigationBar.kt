package com.example.fingid.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.fingid.R
import com.example.fingid.navigation.Screen
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

sealed class BottomNavItem(
    val screen: Screen,
    val titleResId: Int,
    val iconPainterProvider: @Composable () -> Painter
) {
    object Expenses : BottomNavItem(
        Screen.Expenses,
        R.string.bottom_nav_expenses,
        { painterResource(id = R.drawable.ic_downtrend) }
    )
    object Income   : BottomNavItem(
        Screen.Income,
        R.string.bottom_nav_income,
        { painterResource(id = R.drawable.ic_uptrend) }
    )
    object Account  : BottomNavItem(
        Screen.Account,
        R.string.bottom_nav_account,
        { painterResource(id = R.drawable.ic_calculator) }
    )
    object Articles : BottomNavItem(
        Screen.Articles,
        R.string.bottom_nav_articles,
        { painterResource(id = R.drawable.ic_barchartside) }
    )
    object Settings : BottomNavItem(
        Screen.Settings,
        R.string.bottom_nav_settings,
        { painterResource(id = R.drawable.ic_settings) }
    )
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
    val navigationBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier.padding(bottom = navigationBarHeight),
        windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
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
                icon = {
                    val painter = item.iconPainterProvider()
                    Icon(painter = painter, contentDescription = stringResource(item.titleResId))
                },
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