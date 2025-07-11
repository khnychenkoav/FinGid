package com.example.fingid.data.datasource

import com.example.fingid.data.model.AccountBriefDTO
import com.example.fingid.data.model.AccountDTO
import com.example.fingid.data.model.AccountResponseDTO
import com.example.fingid.data.remote.api.FinanceApiService
import com.example.fingid.data.remote.mapper.AccountRemoteMapper
import javax.inject.Inject


interface AccountRemoteDataSource {
    suspend fun getAccountById(accountId: Int): AccountResponseDTO
    suspend fun updateAccountById(accountBrief: AccountBriefDTO): AccountDTO
}


internal class AccountRemoteDataSourceImpl @Inject constructor(
    private val api: FinanceApiService,
    private val mapper: AccountRemoteMapper
) : AccountRemoteDataSource {


    override suspend fun getAccountById(accountId: Int): AccountResponseDTO {
        return mapper.mapAccountResponse(api.getAccountById(accountId))
    }


    override suspend fun updateAccountById(accountBrief: AccountBriefDTO): AccountDTO {
        return mapper.mapAccount(
            api.updateAccountById(
                accountId = accountBrief.id,
                request = mapper.mapAccountBrief(accountBrief)
            )
        )
    }
}