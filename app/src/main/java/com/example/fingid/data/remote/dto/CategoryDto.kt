package com.example.fingid.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    val id: Long,
    val name: String,
    val emoji: String,
    val isIncome: Boolean
)
