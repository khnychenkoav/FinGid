package com.example.fingid.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TransactionResponseDto(
    val id: Long,
    val account: AccountBriefDto,
    val category: CategoryDto,
    val amount: String,
    val transactionDate: String,
    val comment: String? = null,
    val createdAt: String,
    val updatedAt: String
)
