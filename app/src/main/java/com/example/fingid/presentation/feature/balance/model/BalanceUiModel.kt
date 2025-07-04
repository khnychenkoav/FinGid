package com.example.fingid.presentation.feature.balance.model

import com.example.fingid.core.utils.formatWithSpaces

data class BalanceUiModel(
    val id: Int,
    val amount: Int,
    val currency: String
) {
    val balanceFormatted: String
        get() = "${amount.toString().formatWithSpaces()} $currency"
}