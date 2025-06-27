package com.example.fingid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class NavigationState(
    val navHostController: NavHostController
) {
    fun navigateTo(route: String, usePopUpTo: Boolean = true) {
        navHostController.navigate(route) {
            launchSingleTop = true
            restoreState = true
            if (usePopUpTo) {
                popUpTo(navHostController.graph.startDestinationId) {
                    saveState = true
                }
            }
        }
    }
}

@Composable
fun rememberNavigationState(
    navHostController: NavHostController = rememberNavController()
): NavigationState {
    return remember {
        NavigationState(navHostController)
    }
}