package com.example.fingid.navigation

sealed class Screen(val route: String) {
    object Expenses : Screen("expenses_screen")
    object Income : Screen("income_screen")
    object Account : Screen("account_screen")
    object Articles : Screen("articles_screen")
    object Settings : Screen("settings_screen")
    object EditAccount : Screen("edit_account_screen/{balanceValue}") {
        fun createRoute(balanceValue: String) = "edit_account_screen/$balanceValue"
    }
    object AddEditExpense : Screen("add_edit_expense_screen/{expenseId}") {
        fun createRoute(expenseId: String?) = "add_edit_expense_screen/${expenseId ?: "new"}"
    }
    object AddEditIncome : Screen("add_edit_income_screen/{incomeId}") {
        fun createRoute(incomeId: String?) = "add_edit_income_screen/${incomeId ?: "new"}"
    }
    object ExpensesHistory : Screen("expenses_history_screen")

}