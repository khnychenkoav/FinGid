package com.example.fingid.data.model.account

import kotlinx.serialization.Serializable

@Serializable
data class AccountHistory(
    val id: Int,
    val accountId: Int,
    val changeType: ChangeType,
    val previousState: AccountState,
    val newState: AccountState,
    val changeTimestamp: String,
    val createdAt: String
)

enum class ChangeType{
    CREATION, MODIFICATION
}
