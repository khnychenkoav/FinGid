package com.example.fingid.data.model.account

import kotlinx.serialization.Serializable

@Serializable
data class UpdateAccountsRequest(
    val accounts: List<AccountBrief>
)
