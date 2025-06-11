package com.example.fingid.domain.entities

import java.math.BigDecimal

data class StatItem(
    val categoryId: Long,
    val categoryName: String,
    val emoji: String,
    val amount: BigDecimal
)
