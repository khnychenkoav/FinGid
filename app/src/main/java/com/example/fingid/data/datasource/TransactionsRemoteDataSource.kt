package com.example.fingid.data.datasource

import com.example.fingid.data.model.TransactionDTO
import com.example.fingid.data.model.TransactionResponseDTO
import com.example.fingid.data.remote.api.FinanceApiService
import com.example.fingid.data.remote.mapper.TransactionsRemoteMapper
import retrofit2.Response
import javax.inject.Inject


interface TransactionsRemoteDataSource {
    suspend fun getTransactionsByPeriod(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): List<TransactionResponseDTO>

    suspend fun getTransactionById(transactionId: Int): TransactionResponseDTO

    suspend fun createTransaction(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): TransactionDTO

    suspend fun updateTransactionById(
        transactionId: Int,
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): TransactionResponseDTO

    suspend fun deleteTransactionById(transactionId: Int): Response<Void>
}


internal class TransactionsRemoteDataSourceImpl @Inject constructor(
    private val api: FinanceApiService,
    private val mapper: TransactionsRemoteMapper
) : TransactionsRemoteDataSource {


    override suspend fun getTransactionsByPeriod(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): List<TransactionResponseDTO> {
        return api.getTransactionsByPeriod(
            accountId,
            startDate,
            endDate
        ).map(mapper::mapTransactionResponse)
    }

    override suspend fun getTransactionById(transactionId: Int): TransactionResponseDTO {
        return mapper.mapTransactionResponse(api.getTransactionById(transactionId))
    }

    override suspend fun createTransaction(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): TransactionDTO {
        return mapper.mapTransaction(
            api.createTransaction(
                mapper.mapTransactionToRequest(
                    accountId,
                    categoryId,
                    amount,
                    transactionDate,
                    comment
                )
            )
        )
    }

    override suspend fun updateTransactionById(
        transactionId: Int,
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): TransactionResponseDTO {
        return mapper.mapTransactionResponse(
            api.updateTransactionById(
                id = transactionId,
                request = mapper.mapTransactionToRequest(
                    accountId,
                    categoryId,
                    amount,
                    transactionDate,
                    comment
                )
            )
        )
    }


    override suspend fun deleteTransactionById(transactionId: Int): Response<Void> {
        return api.deleteTransactionById(transactionId)
    }
}