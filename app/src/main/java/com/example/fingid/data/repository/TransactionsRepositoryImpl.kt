package com.example.fingid.data.repository

import com.example.fingid.data.datasource.TransactionsLocalDataSource
import com.example.fingid.data.datasource.TransactionsRemoteDataSource
import com.example.fingid.data.local.mappers.transaction.toTransactionEntity
import com.example.fingid.data.local.mappers.transaction.toTransactionModel
import com.example.fingid.data.remote.api.safeApiCall
import com.example.fingid.data.repository.mapper.TransactionsDomainMapper
import com.example.fingid.domain.model.TransactionDomain
import com.example.fingid.domain.model.TransactionResponseDomain
import com.example.fingid.domain.repository.TransactionsRepository
import javax.inject.Inject

internal class TransactionsRepositoryImpl @Inject constructor(
    private val remoteDataSource: TransactionsRemoteDataSource,
    private val localDataSource: TransactionsLocalDataSource,
    private val mapper: TransactionsDomainMapper
) : TransactionsRepository {

    override suspend fun getTransactionsByPeriod(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): Result<List<TransactionResponseDomain>> {
        return try {
            val remoteTransactions = remoteDataSource.getTransactionsByPeriod(accountId, startDate, endDate)

            localDataSource.insertAll(remoteTransactions.map { it.toTransactionEntity(isSynced = true) })

            Result.success(remoteTransactions.map(mapper::mapTransactionResponse))
        } catch (e: Exception) {
            val cachedTransactions = localDataSource.getAll().map { it.toTransactionModel() }

            if (cachedTransactions.isNotEmpty()) {
                Result.success(cachedTransactions)
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun createTransaction(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): Result<TransactionDomain> {
        return safeApiCall(
            call = {
                mapper.mapTransaction(
                    remoteDataSource.createTransaction(
                        accountId,
                        categoryId,
                        amount,
                        transactionDate,
                        comment
                    )
                )
            }
        )
    }

    override suspend fun getTransactionById(transactionId: Int): Result<TransactionResponseDomain> {
        return safeApiCall(
            call = {
                mapper.mapTransactionResponse(
                    remoteDataSource.getTransactionById(transactionId)
                )
            }
        )
    }

    override suspend fun updateTransactionById(
        transactionId: Int,
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): Result<TransactionResponseDomain> {
        return safeApiCall(
            call = {
                mapper.mapTransactionResponse(
                    remoteDataSource.updateTransactionById(
                        transactionId = transactionId,
                        accountId = accountId,
                        categoryId = categoryId,
                        amount = amount,
                        transactionDate = transactionDate,
                        comment = comment
                    )
                )
            }
        )
    }

    override suspend fun deleteTransactionById(transactionId: Int): Result<Unit> {
        localDataSource.delete(transactionId)
        return safeApiCall(
            call = { remoteDataSource.deleteTransactionById(transactionId) },
            handleSuccess = {
                Result.success(Unit)
            }
        )
    }
}