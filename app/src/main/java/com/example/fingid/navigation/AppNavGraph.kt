package com.example.fingid.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.fingid.ui.screens.AddEditExpenseScreen
import com.example.fingid.ui.screens.AddEditIncomeScreen
import com.example.fingid.ui.screens.ExpensesHistoryScreen

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
            ExpensesHistoryScreen(navController = navHostController, isIncome = isIncome)
        }
        composable(Screen.AddEditExpense.createRoute(null)){
            AddEditExpenseScreen(navController = navHostController, expenseId = null)
        }

        composable(Screen.AddEditIncome.createRoute(null)){
            AddEditIncomeScreen(navController = navHostController, incomeId = null)
        }
    }
}