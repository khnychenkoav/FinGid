package com.example.fingid.data.model.account

import kotlinx.serialization.Serializable

@Serializable
data class AccountHistoryResponse (
    val accountId: Int,
    val accountName: String,
    val currency: String,
    val currentBalance: String,
    val history: AccountHistory
)
