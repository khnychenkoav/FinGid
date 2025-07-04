package com.example.fingid.data.remote.model

data class TransactionResponse(
    val id: Int,
    val account: AccountBriefResponse,
    val category: CategoryResponse,
    val amount: String,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String
)
