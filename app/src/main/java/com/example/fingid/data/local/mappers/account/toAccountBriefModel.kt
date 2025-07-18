package com.example.fingid.data.local.mappers.account

import com.example.fingid.data.local.model.AccountEntity
import com.example.fingid.domain.model.AccountBriefDomain

fun AccountEntity.toAccountBriefModel(): AccountBriefDomain = AccountBriefDomain(
    id = id,
    name = name,
    balance = balance.toIntOrNull() ?: 0,
    currency = currency
)