package com.example.fingid.domain.entities

import java.math.BigDecimal

data class AccountState(
    val id: Long,
    val name: String,
    val balance: BigDecimal,
    val currency: String
)
