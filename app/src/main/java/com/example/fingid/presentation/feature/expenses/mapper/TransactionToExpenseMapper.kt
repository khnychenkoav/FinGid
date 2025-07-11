package com.example.fingid.presentation.feature.expenses.mapper

import com.example.fingid.core.utils.formatWithSpaces
import com.example.fingid.domain.model.TransactionResponseDomain
import com.example.fingid.presentation.feature.expenses.model.ExpenseUiModel
import javax.inject.Inject


class TransactionToExpenseMapper @Inject constructor() {
    fun map(domain: TransactionResponseDomain): ExpenseUiModel {
        return ExpenseUiModel(
            id = domain.id,
            title = domain.category.name,
            amount = domain.amount,
            currency = domain.account.getCurrencySymbol(),
            subtitle = domain.comment,
            emoji = domain.category.emoji
        )
    }

    fun calculateTotalAmount(transactions: List<TransactionResponseDomain>): String {
        val total = transactions.sumOf { it.amount }
        val currency = transactions.firstOrNull()?.account?.getCurrencySymbol().orEmpty()
        return "${total.toString().formatWithSpaces()} $currency"
    }
}