package com.example.fingid.data.remote.model

data class AccountUpdateRequest(
    val name: String,
    val balance: String,
    val currency: String
)