package com.example.fingid.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AccountUpdateRequestDto(
    val name: String,
    val balance: String,
    val currency: String
)
