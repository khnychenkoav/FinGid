package com.example.fingid.domain.model

import java.time.LocalDate
import java.time.LocalTime

data class TransactionDomain(
    val id: Int,
    val account: AccountBriefDomain,
    val category: CategoryDomain,
    val amount: Int,
    val transactionDate: LocalDate,
    val transactionTime: LocalTime,
    val comment: String?,
)