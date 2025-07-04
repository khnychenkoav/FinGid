package com.example.fingid.presentation.feature.incomes.mapper

import com.example.fingid.core.utils.formatWithSpaces
import com.example.fingid.domain.model.TransactionDomain
import com.example.fingid.presentation.feature.incomes.model.IncomeUiModel
import javax.inject.Inject


class TransactionToIncomeMapper @Inject constructor() {
    fun map(domain: TransactionDomain): IncomeUiModel {
        return IncomeUiModel(
            id = domain.id,
            title = domain.category.name,
            amount = domain.amount,
            currency = domain.account.getCurrencySymbol(),
            subtitle = domain.comment,
            emoji = domain.category.emoji
        )
    }

    fun calculateTotalAmount(transactions: List<TransactionDomain>): String {
        val total = transactions.sumOf { it.amount }
        val currency = transactions.firstOrNull()?.account?.getCurrencySymbol().orEmpty()
        return "${total.toString().formatWithSpaces()} $currency"
    }
}