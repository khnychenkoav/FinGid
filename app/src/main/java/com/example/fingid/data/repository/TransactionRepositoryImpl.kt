package com.example.fingid.data.repository

import com.example.fingid.data.mapper.toDomain
import com.example.fingid.data.remote.FinanceApiService
import com.example.fingid.domain.model.Transaction
import com.example.fingid.domain.repository.TransactionRepository

class TransactionRepositoryImpl(
    private val api: FinanceApiService
) : TransactionRepository {

    override suspend fun getTransactions(
        accountId: Int,
        from: String,
        to: String
    ): List<Transaction> {
        return api.getTransactionsByAccountAndPeriod(accountId, from, to).map { it.toDomain() }
    }

    override suspend fun createTransaction(transaction: Transaction) {
//        val request = transaction.toCreateRequest()
//        api.createTransaction(request)
    }

    override suspend fun updateTransaction(transaction: Transaction) {
//        val request = transaction.toUpdateRequest()
//        api.updateTransaction(request)
    }

    override suspend fun deleteTransaction(id: Int) {
        //       api.deleteTransaction(DeleteTransactionRequest(id.toString()))
    }
}
