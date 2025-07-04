package com.example.fingid.core.navigation


sealed class Route(val path: String) {

    sealed class Root(path: String) : Route(path) {
        data object Expenses : Root(path = "expenses_screen")
        data object Income : Root(path = "income_screen")
        data object Balance : Root(path = "balance_screen")
        data object Categories : Root(path = "categories_screen")
        data object Settings : Root(path = "settings_screen")
    }


    sealed class SubScreens(path: String) : Route(path) {
        data object History : SubScreens("history_screen/{isIncome}") {
            fun isIncome(): String = "isIncome"
            fun route(income: Boolean) = "history_screen/$income"
        }

        data object BalanceEdit : SubScreens("balance_edit/{balanceId}") {
            fun balanceId(): String = "balanceId"
            fun route(balanceId: Int) = "balance_edit/$balanceId"
        }
    }
}