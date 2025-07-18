package com.example.fingid.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.fingid.presentation.feature.analysis.screen.AnalysisScreen
import com.example.fingid.presentation.feature.balance.screen.BalanceScreen
import com.example.fingid.presentation.feature.balance_update.screen.BalanceUpdateScreen
import com.example.fingid.presentation.feature.categories.screen.CategoriesScreen
import com.example.fingid.presentation.feature.expenses.screen.ExpensesScreen
import com.example.fingid.presentation.feature.history.screen.HistoryScreen
import com.example.fingid.presentation.feature.incomes.screen.IncomeScreen
import com.example.fingid.presentation.feature.main.model.ScreenConfig
import com.example.fingid.presentation.feature.settings.screen.SettingsScreen
import com.example.fingid.presentation.feature.transaction_creation.screen.TransactionCreationScreen
import com.example.fingid.presentation.feature.transaction_update.screen.TransactionUpdateScreen


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
            ExpensesScreen(
                updateConfigState = updateConfigState,
                onHistoryNavigate = {
                    navController.navigate(Route.SubScreens.ExpensesHistory.path) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onCreateNavigate = {
                    navController.navigate(Route.SubScreens.ExpenseTransactionCreation.path) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onTransactionUpdateNavigate = {
                    navController.navigate(Route.SubScreens.ExpenseTransactionUpdate.route(it)) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }


        composable(route = Route.Root.Income.path) {
            IncomeScreen(
                updateConfigState = updateConfigState,
                onHistoryNavigate = {
                    navController.navigate(Route.SubScreens.IncomesHistory.path) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onCreateNavigate = {
                    navController.navigate(Route.SubScreens.IncomeTransactionCreation.path) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onTransactionUpdateNavigate = {
                    navController.navigate(Route.SubScreens.IncomeTransactionUpdate.route(it)) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }


        composable(route = Route.Root.Balance.path) {
            BalanceScreen(
                updateConfigState = updateConfigState,
                onEditNavigate = {
                    navController.navigate(Route.SubScreens.BalanceEdit.route(it)) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }


        composable(route = Route.Root.Categories.path) {
            CategoriesScreen(updateConfigState = updateConfigState)
        }


        composable(route = Route.Root.Settings.path) {
            SettingsScreen(updateConfigState = updateConfigState)
        }


        composable(route = Route.SubScreens.ExpensesHistory.path) {
            HistoryScreen(
                isIncome = false,
                updateConfigState = updateConfigState,
                onBackNavigate = { navController.popBackStack() },
                onAnalysisNavigate = {
                    navController.navigate(Route.SubScreens.ExpensesAnalysis.path)
                }
            )
        }


        composable(route = Route.SubScreens.IncomesHistory.path) {
            HistoryScreen(
                isIncome = true,
                updateConfigState = updateConfigState,
                onBackNavigate = { navController.popBackStack() },
                onAnalysisNavigate = {
                    navController.navigate(Route.SubScreens.IncomesAnalysis.path)
                }
            )
        }

        composable(route = Route.SubScreens.ExpensesAnalysis.path) {
            AnalysisScreen(
                isIncome = false,
                updateConfigState = updateConfigState,
                onBackNavigate = { navController.popBackStack() }
            )
        }

        composable(route = Route.SubScreens.IncomesAnalysis.path) {
            AnalysisScreen(
                isIncome = true,
                updateConfigState = updateConfigState,
                onBackNavigate = { navController.popBackStack() }
            )
        }

        composable(route = Route.SubScreens.ExpenseTransactionCreation.path) {
            TransactionCreationScreen(
                isIncome = false,
                updateConfigState = updateConfigState,
                onBackNavigate = { navController.popBackStack() }
            )
        }


        composable(route = Route.SubScreens.IncomeTransactionCreation.path) {
            TransactionCreationScreen(
                isIncome = true,
                updateConfigState = updateConfigState,
                onBackNavigate = { navController.popBackStack() }
            )
        }


        composable(
            route = Route.SubScreens.ExpenseTransactionUpdate.path,
            arguments = listOf(navArgument(Route.SubScreens.ExpenseTransactionUpdate.transactionId()) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getString(
                Route.SubScreens.ExpenseTransactionUpdate.transactionId()
            ) ?: ""
            TransactionUpdateScreen(
                transactionId = transactionId,
                isIncome = false,
                updateConfigState = updateConfigState,
                onBackNavigate = { navController.popBackStack() }
            )
        }

        composable(
            route = Route.SubScreens.IncomeTransactionUpdate.path,
            arguments = listOf(navArgument(Route.SubScreens.IncomeTransactionUpdate.transactionId()) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getString(
                Route.SubScreens.IncomeTransactionUpdate.transactionId()
            ) ?: ""
            TransactionUpdateScreen(
                transactionId = transactionId,
                isIncome = true,
                updateConfigState = updateConfigState,
                onBackNavigate = { navController.popBackStack() }
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
            BalanceUpdateScreen(
                balanceId = balanceId,
                updateConfigState = updateConfigState,
                onBackNavigate = { navController.popBackStack() }
            )
        }
    }
}