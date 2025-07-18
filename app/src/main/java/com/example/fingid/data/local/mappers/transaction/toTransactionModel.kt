package com.example.fingid.data.local.mappers.transaction

import com.example.fingid.data.local.mappers.account.toAccountBriefModel
import com.example.fingid.data.local.mappers.category.toCategoryModel
import com.example.fingid.data.local.model.TransactionWithRelations
import com.example.fingid.domain.model.TransactionResponseDomain
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun TransactionWithRelations.toTransactionModel(): TransactionResponseDomain {
    val localDateTime = LocalDateTime.parse(transaction.transactionDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    return TransactionResponseDomain(
        id = transaction.id,
        account = accountModel.toAccountBriefModel(),
        category = categoryModel.toCategoryModel(),
        amount = transaction.amount.toIntOrNull() ?: 0,
        transactionDate = localDateTime.toLocalDate(),
        transactionTime = localDateTime.toLocalTime(),
        comment = transaction.comment
    )
}