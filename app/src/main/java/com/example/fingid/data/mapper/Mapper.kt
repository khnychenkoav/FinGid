package com.example.fingid.data.mapper

import com.example.fingid.data.model.account.AccountBrief
import com.example.fingid.data.model.account.AccountDto
import com.example.fingid.data.model.category.CategoryDto
import com.example.fingid.data.model.transaction.TransactionDto
import com.example.fingid.domain.model.Account
import com.example.fingid.domain.model.Category
import com.example.fingid.domain.model.Transaction

fun TransactionDto.toDomain(): Transaction = Transaction(
    id = id,
    accountId = account.id.toString(),
    categoryEmoji = category.emoji,
    categoryName = category.name,
    isIncome = category.isIncome,
    amount = amount.toDoubleOrNull() ?: 0.0,
    time = transactionDate,
    comment = comment
)

fun Account.toAccountBrief(): AccountBrief = AccountBrief(
    id = id.toInt(),
    name = name,
    balance = balance.toString(),
    currency = currency
)

fun AccountDto.toDomain(): Account = Account(
    id = this.id,
    name = this.name,
    balance = this.balance.toDouble(),
    currency = this.currency
)

fun CategoryDto.mapToCategory(): Category{
    return Category(
        id = this.id,
        iconLeading = this.emoji,
        textLeading = this.name,
        isIncome = this.isIncome
    )
}
