package com.example.fingid.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AccountHistoryDto(
    val id: Long,
    val accountId: Long,
    val changeType: String, // "CREATION" | "MODIFICATION"
    val previousState: AccountStateDto? = null,
    val newState: AccountStateDto,
    val changeTimestamp: String,
    val createdAt: String
)
