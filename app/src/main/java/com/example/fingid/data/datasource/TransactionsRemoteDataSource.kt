package com.example.fingid.data.datasource

import com.example.fingid.data.model.TransactionDTO
import com.example.fingid.data.remote.api.FinanceApiService
import com.example.fingid.data.remote.mapper.TransactionsRemoteMapper
import javax.inject.Inject


interface TransactionsRemoteDataSource {
    suspend fun getTransactionsByPeriod(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): List<TransactionDTO>
}


internal class TransactionsRemoteDataSourceImpl @Inject constructor(
    private val api: FinanceApiService,
    private val mapper: TransactionsRemoteMapper
) : TransactionsRemoteDataSource {


    override suspend fun getTransactionsByPeriod(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): List<TransactionDTO> {
        return api.getTransactionsByPeriod(
            accountId,
            startDate,
            endDate
        ).map(mapper::mapTransaction)
    }
}
