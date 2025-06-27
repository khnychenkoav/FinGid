package com.example.fingid.data.model.account

import kotlinx.serialization.Serializable

@Serializable
data class AccountState (
    val id: Int,
    val name: String,
    val balance: String,
    val currency: String
)
