package com.example.fingid.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class History(
    val id: Int,
    val leadingIcon:String,
    val leadingName:String,
    val leadingComment: String?,
    val trailingPrice: Double,
    val trailingTime: String
)
