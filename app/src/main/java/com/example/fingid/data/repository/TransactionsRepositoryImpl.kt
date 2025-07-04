package com.example.fingid.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.fingid.data.datasource.TransactionsRemoteDataSource
import com.example.fingid.data.remote.api.safeApiCall
import com.example.fingid.data.repository.mapper.TransactionsDomainMapper
import com.example.fingid.domain.model.TransactionDomain
import com.example.fingid.domain.repository.TransactionsRepository
import javax.inject.Inject


internal class TransactionsRepositoryImpl @Inject constructor(
    private val remoteDataSource: TransactionsRemoteDataSource,
    private val mapper: TransactionsDomainMapper
) : TransactionsRepository {


    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getTransactionsByPeriod(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): Result<List<TransactionDomain>> {
        return safeApiCall {
            remoteDataSource.getTransactionsByPeriod(accountId, startDate, endDate)
                .map(mapper::mapTransaction)
        }
    }
}