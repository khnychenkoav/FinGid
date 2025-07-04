package com.example.fingid.domain.repository

import com.example.fingid.domain.model.TransactionDomain


interface TransactionsRepository {
    suspend fun getTransactionsByPeriod(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): Result<List<TransactionDomain>>
}