package com.example.fingid.domain.entities

import java.math.BigDecimal
import java.time.OffsetDateTime

data class Transaction(
    val id: Long,
    val account: AccountBrief,
    val category: Category,
    val amount: BigDecimal,
    val transactionDate: OffsetDateTime,
    val comment: String?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)

data class AccountBrief(
    val id: Long,
    val name: String,
    val balance: BigDecimal,
    val currency: String
)
