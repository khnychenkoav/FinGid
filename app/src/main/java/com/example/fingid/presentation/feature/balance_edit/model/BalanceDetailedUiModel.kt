package com.example.fingid.presentation.feature.balance_edit.model

data class BalanceDetailedUiModel(
    val id: Int,
    val name: String,
    val amount: String,
    val currencyCode: String,
    val currencySymbol: String
)