package com.example.fingid.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AccountHistoryResponseDto(
    val accountId: Long,
    val accountName: String,
    val currency: String,
    val currentBalance: String,
    val history: List<AccountHistoryDto>
)
