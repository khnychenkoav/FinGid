package com.example.fingid.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    expensesScreenContent: @Composable () -> Unit,
    incomeScreenContent: @Composable () -> Unit,
    accountScreenContent: @Composable () -> Unit,
    articlesScreenContent: @Composable () -> Unit,
    settingsScreenContent: @Composable () -> Unit,
    historyScreenContent: @Composable (Boolean) -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Expenses.route
    ) {
        composable(Screen.Expenses.route) { expensesScreenContent() }
        composable(Screen.Income.route) { incomeScreenContent() }
        composable(Screen.Account.route) { accountScreenContent() }
        composable(Screen.Articles.route) { articlesScreenContent() }
        composable(Screen.Settings.route) { settingsScreenContent() }

        composable(
            route = Screen.History.route,
            arguments = listOf(navArgument("isIncome") { type = NavType.BoolType })
        ) { backStackEntry ->
            val isIncome = backStackEntry.arguments?.getBoolean("isIncome") ?: false
            historyScreenContent(isIncome)
        }
    }
}