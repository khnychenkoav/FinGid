package com.example.fingid.data.local.mappers.account

import com.example.fingid.data.local.model.AccountEntity
import com.example.fingid.domain.model.AccountBriefDomain

fun AccountBriefDomain.toAccountEntity(): AccountEntity = AccountEntity(
    id = id,
    name = name,
    balance = balance.toString(),
    currency = currency
)