package com.example.fingid.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AccountStateDto(
    val id: Long,
    val name: String,
    val balance: String,
    val currency: String
)
