package com.example.fingid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fingid.navigation.Screen
import com.example.fingid.ui.components.AppBottomNavigationBar
import com.example.fingid.ui.screens.AccountScreen
import com.example.fingid.ui.screens.ArticlesScreen
import com.example.fingid.ui.screens.EditAccountScreen
import com.example.fingid.ui.screens.SettingsScreen
import com.example.fingid.ui.theme.FinGidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            FinGidTheme {
                AppRoot()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRoot() {
    val navController = rememberNavController()
    var currentAccountBalance by remember { mutableStateOf("-670 000 ₽") }

    Column(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = Screen.Settings.route,
            modifier = Modifier.weight(1f)
        ) {
            composable(Screen.Expenses.route) { SimpleScreenContent("Экран Расходов") }
            composable(Screen.Income.route) { SimpleScreenContent("Экран Доходов") }
            composable(Screen.Account.route) {
                AccountScreen(
                    navController = navController,
                    currentBalance = currentAccountBalance
                )
            }
            composable(Screen.Articles.route) {
                ArticlesScreen()
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
            composable(
                route = Screen.EditAccount.route,
                arguments = listOf(navArgument("balanceValue") { type = NavType.StringType })
            ) { backStackEntry ->
                val balanceValue = backStackEntry.arguments?.getString("balanceValue") ?: ""
                EditAccountScreen(
                    navController = navController,
                    initialBalance = balanceValue,
                    onSaveAccount = { newBalance ->
                        currentAccountBalance = newBalance
                        println("Баланс сохранен: $newBalance")
                    },
                    onDeleteAccount = {
                        println("Счет удален!")
                        currentAccountBalance = "0 ₽"
                    }
                )
            }
        }
        AppBottomNavigationBar(navController = navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleScreenContent(title: String) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Содержимое экрана: $title")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FinGidTheme {
        AppRoot()
    }
}