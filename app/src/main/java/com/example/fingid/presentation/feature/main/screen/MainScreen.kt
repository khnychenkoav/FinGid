package com.example.fingid.presentation.feature.main.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fingid.core.di.daggerViewModel
import com.example.fingid.core.navigation.AppNavHost
import com.example.fingid.core.navigation.BottomBarItem
import com.example.fingid.presentation.feature.main.component.BottomNavigationBar
import com.example.fingid.presentation.feature.main.component.CustomFloatingActionButton
import com.example.fingid.presentation.feature.main.component.CustomTopBar
import com.example.fingid.presentation.feature.main.viewmodel.MainScreenViewModel

@Composable
fun MainScreen() {
    val viewModel: MainScreenViewModel = daggerViewModel()
    val configState by viewModel.configState.collectAsState()

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            CustomTopBar(config = configState.topBarConfig)
        },
        bottomBar = {
            BottomNavigationBar(
                currentDestination = currentDestination,
                items = BottomBarItem.items,
                onNavigate = {
                    if (currentDestination != it) {
                        navController.navigate(it) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }

                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            configState.floatingActionConfig?.let { action ->
                CustomFloatingActionButton(
                    description = action.descriptionResId,
                    onClick = { action.actionUnit.invoke() },
                )
            }
        }
    ) { innerPadding ->
        AppNavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            updateConfigState = { config -> viewModel.updateConfigForScreen(config) }
        )
    }
}