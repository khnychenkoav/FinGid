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
        data object ExpensesHistory : Root(path = "history_screen/expenses")
        data object IncomesHistory : Root(path = "history_screen/incomes")

        data object ExpenseTransactionCreation : Root(path = "transaction_creation_screen/expense")
        data object IncomeTransactionCreation : Root(path = "transaction_creation_screen/income")

        data object BalanceEdit : SubScreens("balance_edit/{balanceId}") {
            fun balanceId(): String = "balanceId"
            fun route(balanceId: Int) = "balance_edit/$balanceId"
        }

        data object IncomeTransactionUpdate : SubScreens(
            "transaction_update_screen/income/{id}"
        ) {
            fun transactionId(): String = "id"
            fun route(transactionId: Int) = "transaction_update_screen/income/$transactionId"
        }

        data object ExpenseTransactionUpdate : SubScreens(
            "transaction_update_screen/expense/{id}"
        ) {
            fun transactionId(): String = "id"
            fun route(transactionId: Int) = "transaction_update_screen/expense/$transactionId"
        }

        data object ExpensesAnalysis : Root(path = "analysis_screen/expenses")
        data object IncomesAnalysis : Root(path = "analysis_screen/incomes")

        data object About : Route("about_screen")
    }
}