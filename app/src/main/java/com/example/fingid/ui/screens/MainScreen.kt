package com.example.fingid.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.fingid.R
import com.example.fingid.data.repository.RepositoryProvider
import com.example.fingid.domain.repository.AccountRepository
import com.example.fingid.domain.repository.CategoryRepository
import com.example.fingid.domain.repository.TransactionRepository
import com.example.fingid.navigation.AppNavGraph
import com.example.fingid.navigation.Screen
import com.example.fingid.navigation.rememberNavigationState
import com.example.fingid.ui.components.AppBottomNavigationBar

val LocalAccountRepository = staticCompositionLocalOf<AccountRepository> { error("No AccountRepository provided") }
val LocalTransactionRepository = staticCompositionLocalOf<TransactionRepository> { error("No TransactionRepository provided") }
val LocalCategoryRepository = staticCompositionLocalOf<CategoryRepository> { error("No CategoryRepository provided") }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navigationState = rememberNavigationState()
    val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    CompositionLocalProvider(
        LocalAccountRepository provides RepositoryProvider.getAccountRepository(),
        LocalTransactionRepository provides RepositoryProvider.getTransactionRepository(),
        LocalCategoryRepository provides RepositoryProvider.getCategoryRepository()
    ) {
        Scaffold(
            topBar = {
                if (currentRoute != null && Screen.needShowTopBar(currentRoute)) {
                    TopAppBar(
                        title = { Text(Screen.getTitleByRoute(currentRoute)) },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        actions = {
                            if (currentRoute == Screen.Expenses.route) {
                                IconButton(onClick = { navigationState.navHostController.navigate(Screen.ExpensesHistory.route) }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_trailng_clock),
                                        contentDescription = "История расходов"
                                    )
                                }
                            }
                        }
                    )
                }
            },
            bottomBar = {
                AppBottomNavigationBar(navController = navigationState.navHostController)
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                AppNavGraph(
                    navHostController = navigationState.navHostController,
                    expensesScreenContent = { ExpensesScreen(navController = navigationState.navHostController) },
                    incomeScreenContent = { IncomeScreen(navController = navigationState.navHostController) },
                    accountScreenContent = {
                        val viewModelFactory = AccountViewModelFactory(1L) // Временно хардкод ID счета
                        AccountScreen(
                            navController = navigationState.navHostController,
                            viewModel = viewModel(factory = viewModelFactory)
                        )
                    },
                    articlesScreenContent = { ArticlesScreen() },
                    settingsScreenContent = { SettingsScreen() },
                    historyScreenContent = { isIncome ->
                        ExpensesHistoryScreen(navController = navigationState.navHostController, isIncome = isIncome)
                    }
                )
            }
        }
    }
}