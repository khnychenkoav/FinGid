package com.example.fingid.navigation

sealed class Screen(val route: String) {
    object Expenses : Screen("expenses_screen")
    object Income : Screen("income_screen")
    object Account : Screen("account_screen")
    object Articles : Screen("articles_screen")
    object Settings : Screen("settings_screen")
    object EditAccount : Screen("account_edit_screen/{balanceValue}") {
        fun createRoute(balanceValue: String) = "account_edit_screen/$balanceValue"
    }
    object AddEditExpense : Screen("expenses_add_edit_expense_screen/{expenseId}") {
        fun createRoute(expenseId: String?) = "expenses_add_edit_expense_screen/${expenseId ?: "new"}"
    }
    object AddEditIncome : Screen("income_add_edit_income_screen/{incomeId}") {
        fun createRoute(incomeId: String?) = "income_add_edit_income_screen/${incomeId ?: "new"}"
    }
    object ExpensesHistory : Screen("expenses_history_screen")
    object Analysis : Screen("expenses_analysis_screen/{start}/{end}") {
        fun createRoute(start: String, end: String) =
            "expenses_analysis_screen/$start/$end"
    }
}