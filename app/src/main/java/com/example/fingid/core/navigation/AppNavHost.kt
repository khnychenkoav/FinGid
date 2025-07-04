package com.example.fingid.core.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.fingid.presentation.feature.balance.screen.BalanceScreen
import com.example.fingid.presentation.feature.balance_edit.screen.BalanceEditScreen
import com.example.fingid.presentation.feature.categories.screen.CategoriesScreen
import com.example.fingid.presentation.feature.expenses.screen.ExpensesScreen
import com.example.fingid.presentation.feature.history.screen.HistoryScreen
import com.example.fingid.presentation.feature.incomes.screen.IncomeScreen
import com.example.fingid.presentation.feature.main.model.ScreenConfig
import com.example.fingid.presentation.feature.settings.screen.SettingsScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    updateConfigState: (ScreenConfig) -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Route.Root.Expenses.path
    ) {

        composable(route = Route.Root.Expenses.path) {
            ExpensesScreen(updateConfigState = updateConfigState)
        }


        composable(route = Route.Root.Income.path) {
            IncomeScreen(updateConfigState = updateConfigState)
        }


        composable(route = Route.Root.Balance.path) {
            BalanceScreen(updateConfigState = updateConfigState)
        }

        composable(route = Route.Root.Categories.path) {
            CategoriesScreen(updateConfigState = updateConfigState)
        }

        composable(route = Route.Root.Settings.path) {
            SettingsScreen(updateConfigState = updateConfigState)
        }

        composable(
            route = Route.SubScreens.History.path,
            arguments = listOf(navArgument(Route.SubScreens.History.isIncome()) {
                type = NavType.BoolType
            })
        ) { backStackEntry ->
            val isIncome = backStackEntry.arguments?.getBoolean(
                Route.SubScreens.History.isIncome()
            ) ?: false
            HistoryScreen(
                isIncome = isIncome,
                updateConfigState = updateConfigState
            )
        }


        composable(
            route = Route.SubScreens.BalanceEdit.path,
            arguments = listOf(navArgument(Route.SubScreens.BalanceEdit.balanceId()) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val balanceId = backStackEntry.arguments?.getString(
                Route.SubScreens.BalanceEdit.balanceId()
            ) ?: ""
            BalanceEditScreen(
                balanceId = balanceId,
                updateConfigState = updateConfigState
            )
        }
    }
}