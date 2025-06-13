package com.example.fingid.data.remote.mapper

import com.example.fingid.data.remote.dto.*
import com.example.fingid.domain.entities.*
import java.math.BigDecimal
import java.time.OffsetDateTime

private fun String.asMoney()      = BigDecimal(this)
private fun String.asDateTime()   = OffsetDateTime.parse(this)

fun AccountDto.toDomain() = Account(
    id         = id,
    name       = name,
    balance    = balance.asMoney(),
    currency   = currency,
    createdAt  = createdAt.asDateTime(),
    updatedAt  = updatedAt.asDateTime()
)

fun AccountStateDto.toDomain() = AccountState(
    id       = id,
    name     = name,
    balance  = balance.asMoney(),
    currency = currency
)

fun AccountHistoryDto.toDomain() = AccountHistory(
    id             = id,
    accountId      = accountId,
    changeType     = ChangeType.valueOf(changeType),
    previousState  = previousState?.toDomain(),
    newState       = newState.toDomain(),
    changeTimestamp= changeTimestamp.asDateTime(),
    createdAt      = createdAt.asDateTime()
)

fun StatItemDto.toDomain() = StatItem(
    categoryId   = categoryId,
    categoryName = categoryName,
    emoji        = emoji,
    amount       = amount.asMoney()
)

fun AccountResponseDto.toDomain() = Account(
    id        = id,
    name      = name,
    balance   = balance.asMoney(),
    currency  = currency,
    createdAt = createdAt.asDateTime(),
    updatedAt = updatedAt.asDateTime()
)

fun CategoryDto.toDomain() = Category(id, name, emoji, isIncome)

fun AccountBriefDto.toDomain() = AccountBrief(
    id       = id,
    name     = name,
    balance  = balance.asMoney(),
    currency = currency
)

fun TransactionResponseDto.toDomain() = Transaction(
    id              = id,
    account         = account.toDomain(),
    category        = category.toDomain(),
    amount          = amount.asMoney(),
    transactionDate = transactionDate.asDateTime(),
    comment         = comment,
    createdAt       = createdAt.asDateTime(),
    updatedAt       = updatedAt.asDateTime()
)
