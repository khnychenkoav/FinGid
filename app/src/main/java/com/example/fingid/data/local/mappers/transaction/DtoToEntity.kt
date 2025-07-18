package com.example.fingid.data.local.mappers.transaction

import com.example.fingid.data.local.model.TransactionEntity
import com.example.fingid.data.model.TransactionResponseDTO

fun TransactionResponseDTO.toTransactionEntity(isSynced: Boolean): TransactionEntity {
    return TransactionEntity(
        id = id,
        accountId = account.id,
        categoryId = category.id,
        amount = amount,
        transactionDate = transactionDate,
        comment = comment,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isSynced = isSynced
    )
}