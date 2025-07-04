package com.example.fingid.presentation.feature.history.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.fingid.core.utils.formatDateAndTime
import com.example.fingid.core.utils.formatWithSpaces
import com.example.fingid.domain.model.TransactionDomain
import com.example.fingid.presentation.feature.history.model.TransactionUiModel
import javax.inject.Inject


class TransactionToTransactionUiMapper @Inject constructor() {
    @RequiresApi(Build.VERSION_CODES.O)
    fun map(domain: TransactionDomain): TransactionUiModel {
        return TransactionUiModel(
            id = domain.id,
            title = domain.category.name,
            amount = domain.amount,
            currency = domain.account.getCurrencySymbol(),
            subtitle = domain.comment,
            emoji = domain.category.emoji,
            transactionAt = formatDateAndTime(domain.transactionTime, domain.transactionDate)
        )
    }

    fun calculateTotalAmount(transactions: List<TransactionDomain>): String {
        val total = transactions.sumOf { it.amount }
        val currency = transactions.firstOrNull()?.account?.getCurrencySymbol().orEmpty()
        return "${total.toString().formatWithSpaces()} $currency"
    }
}