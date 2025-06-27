package com.example.fingid.data.model.account

import kotlinx.serialization.Serializable

@Serializable
class AccountBrief (
    val id: Int,
    val name: String,
    val balance: String,
    val currency: String
)
