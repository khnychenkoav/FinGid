package com.example.fingid.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class StatItemDto(
    val categoryId: Long,
    val categoryName: String,
    val emoji: String,
    val amount: String
)
