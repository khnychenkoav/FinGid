package com.example.fingid.domain.entities

import java.math.BigDecimal
import java.time.OffsetDateTime

data class Account(
    val id: Long,
    val name: String,
    val balance: BigDecimal,
    val currency: String,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)
