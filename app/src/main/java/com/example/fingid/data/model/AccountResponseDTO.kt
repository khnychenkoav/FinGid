package com.example.fingid.data.model


data class AccountResponseDTO(
    val id: Int,
    val name: String,
    val balance: String,
    val currency: String,
    val incomeStats: List<StatItemDTO>,
    val expenseStats: List<StatItemDTO>,
    val createdAt: String,
    val updatedAt: String
)