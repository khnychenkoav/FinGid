package com.example.fingid.domain.entities

import java.time.OffsetDateTime

data class AccountHistory(
    val id: Long,
    val accountId: Long,
    val changeType: ChangeType,
    val previousState: AccountState?,
    val newState: AccountState,
    val changeTimestamp: OffsetDateTime,
    val createdAt: OffsetDateTime
)
