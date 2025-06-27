package com.example.fingid.data.model.transaction

import com.example.fingid.data.model.category.CategoryDto
import com.example.fingid.data.model.account.AccountBrief
import kotlinx.serialization.Serializable

@Serializable
data class TransactionDto(
    val id: Int,
    val account: AccountBrief,
    val category: CategoryDto,
    val amount: String,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String
)
