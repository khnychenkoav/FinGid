package com.example.fingid.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AccountBriefDto(
    val id: Long,
    val name: String,
    val balance: String,
    val currency: String
)
