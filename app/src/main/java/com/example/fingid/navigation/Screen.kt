package com.example.fingid.navigation

sealed class Screen(val route: String) {
    object Expenses : Screen("expenses_screen")
    object Income : Screen("income_screen")
    object Account : Screen("account_screen")
    object Articles : Screen("articles_screen")
    object Settings : Screen("settings_screen")
}