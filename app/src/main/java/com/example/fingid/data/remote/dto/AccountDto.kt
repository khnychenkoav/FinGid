package com.example.fingid.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AccountDto(
    val id: Long,
    val userId: Long? = null,
    val name: String,
    val balance: String,
    val currency: String,
    val createdAt: String,
    val updatedAt: String
)
