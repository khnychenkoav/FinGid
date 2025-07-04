package com.example.fingid.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.fingid.data.datasource.AccountRemoteDataSource
import com.example.fingid.data.remote.api.safeApiCall
import com.example.fingid.data.repository.mapper.AccountDomainMapper
import com.example.fingid.domain.model.AccountBriefDomain
import com.example.fingid.domain.model.AccountDomain
import com.example.fingid.domain.repository.AccountRepository
import javax.inject.Inject


internal class AccountRepositoryImpl @Inject constructor(
    private val remoteDataSource: AccountRemoteDataSource,
    private val mapper: AccountDomainMapper
) : AccountRepository {


    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getAccountById(accountId: Int): Result<AccountDomain> {
        return safeApiCall {
            mapper.mapAccount(remoteDataSource.getAccountById(accountId))
        }
    }

    override suspend fun updateAccountById(accountBrief: AccountBriefDomain) {
        safeApiCall {
            remoteDataSource.updateAccountById(
                accountBrief = mapper.mapAccountBrief(accountBrief)
            )
        }
    }
}