package com.example.fingid.data.repository

import com.example.fingid.data.datasource.AccountRemoteDataSource
import com.example.fingid.data.remote.api.safeApiCall
import com.example.fingid.data.repository.mapper.AccountDomainMapper
import com.example.fingid.domain.model.AccountBriefDomain
import com.example.fingid.domain.model.AccountDomain
import com.example.fingid.domain.model.AccountResponseDomain
import com.example.fingid.domain.repository.AccountRepository
import javax.inject.Inject


internal class AccountRepositoryImpl @Inject constructor(
    private val remoteDataSource: AccountRemoteDataSource,
    private val mapper: AccountDomainMapper
) : AccountRepository {


    override suspend fun getAccountById(accountId: Int): Result<AccountResponseDomain> {
        return safeApiCall(
            call = { mapper.mapAccountResponse(remoteDataSource.getAccountById(accountId)) }
        )
    }


    override suspend fun updateAccountById(
        accountBrief: AccountBriefDomain
    ): Result<AccountDomain> {
        return safeApiCall(
            call = {
                mapper.mapAccount(
                    remoteDataSource.updateAccountById(
                        accountBrief = mapper.mapAccountBrief(accountBrief)
                    )
                )
            }
        )
    }
}