package com.example.fingid.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AccountResponseDto(
    val id: Long,
    val name: String,
    val balance: String,
    val currency: String,
    val incomeStats: List<StatItemDto>,
    val expenseStats: List<StatItemDto>,
    val createdAt: String,
    val updatedAt: String
)
