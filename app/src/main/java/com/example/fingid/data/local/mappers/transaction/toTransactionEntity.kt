package com.example.fingid.data.local.mappers.transaction

import com.example.fingid.data.local.model.TransactionEntity
import com.example.fingid.domain.model.TransactionResponseDomain
import java.time.format.DateTimeFormatter

fun TransactionResponseDomain.toTransactionEntity(isSynced: Boolean): TransactionEntity {
    val isoDateTime = transactionDate.atTime(transactionTime)
        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    return TransactionEntity(
        id = id,
        accountId = account.id,
        categoryId = category.id,
        amount = amount.toString(),
        transactionDate = isoDateTime,
        comment = comment,
        createdAt = isoDateTime,
        updatedAt = isoDateTime,
        isSynced = isSynced
    )
}