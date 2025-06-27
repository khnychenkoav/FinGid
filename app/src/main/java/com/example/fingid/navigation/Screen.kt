package com.example.fingid.navigation

sealed class Screen(val route: String) {
    object Expenses : Screen("expenses_screen")
    object Income : Screen("income_screen")
    object Account : Screen("account_screen/{accountId}") {
        fun createRoute(accountId: Long) = "account_screen/$accountId"
    }
    object Articles : Screen("articles_screen")
    object Settings : Screen("settings_screen")
    object EditAccount : Screen("account_edit_screen?accountId={accountId}&balanceValue={balanceValue}") {
        fun createRoute(accountId: Long? = null, balanceValue: String) =
            "account_edit_screen?accountId=${accountId?.toString() ?: "new"}&balanceValue=$balanceValue"
    }
    object AddEditExpense : Screen("expenses_add_edit_expense_screen/{expenseId}") {
        fun createRoute(expenseId: String?) = "expenses_add_edit_expense_screen/${expenseId ?: "new"}"
    }
    object AddEditIncome : Screen("income_add_edit_income_screen/{incomeId}") {
        fun createRoute(incomeId: String?) = "income_add_edit_income_screen/${incomeId ?: "new"}"
    }
    object ExpensesHistory : Screen("expenses_history_screen")
    object History : Screen("history/{isIncome}") {
        fun createRoute(isIncome: Boolean) = "history/$isIncome"
    }
    object Analysis : Screen("expenses_analysis_screen/{start}/{end}") {
        fun createRoute(start: String, end: String) =
            "expenses_analysis_screen/$start/$end"
    }
    companion object {
        fun getTitleByRoute(route: String): String {
            return when (route) {
                Expenses.route -> "Расходы сегодня"
                Income.route -> "Доходы сегодня"
                Account.route -> "Мой счет"
                Articles.route -> "Мои статьи"
                Settings.route -> "Настройки"
                ExpensesHistory.route -> "История расходов"
                else -> ""
            }
        }

        fun needShowTopBar(route: String): Boolean {
            return false
        }
    }
}