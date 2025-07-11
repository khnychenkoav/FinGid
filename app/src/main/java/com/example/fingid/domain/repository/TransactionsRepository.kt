package com.example.fingid.domain.repository

import com.example.fingid.domain.model.TransactionDomain
import com.example.fingid.domain.model.TransactionResponseDomain



interface TransactionsRepository {
    suspend fun getTransactionsByPeriod(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): Result<List<TransactionResponseDomain>>

    suspend fun createTransaction(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): Result<TransactionDomain>

    suspend fun getTransactionById(transactionId: Int): Result<TransactionResponseDomain>

    suspend fun updateTransactionById(
        transactionId: Int,
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): Result<TransactionResponseDomain>

    suspend fun deleteTransactionById(transactionId: Int): Result<Unit>
}