package com.example.fingid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fingid.navigation.Screen
import com.example.fingid.ui.components.AppBottomNavigationBar
import com.example.fingid.ui.screens.SettingsScreen
import com.example.fingid.ui.theme.FinGidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinGidTheme {
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            AppBottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Expenses.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Expenses.route) {
                // TODO: Позже здесь будет ExpensesScreen()
                Text("Экран Расходов")
            }
            composable(Screen.Income.route) {
                // TODO: Позже здесь будет IncomeScreen()
                Text("Экран Доходов")
            }
            composable(Screen.Account.route) {
                // TODO: Позже здесь будет AccountScreen()
                Text("Экран Счета")
            }
            composable(Screen.Articles.route) {
                // TODO: Позже здесь будет ArticlesScreen()
                Text("Экран Статей")
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FinGidTheme {
        AppContent()
    }
}