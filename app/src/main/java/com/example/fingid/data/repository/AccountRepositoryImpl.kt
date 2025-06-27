package com.example.fingid.data.repository

import com.example.fingid.data.mapper.toAccountBrief
import com.example.fingid.data.mapper.toDomain
import com.example.fingid.data.model.account.UpdateAccountsRequest
import com.example.fingid.domain.model.Account
import com.example.fingid.domain.repository.AccountRepository
import com.example.fingid.data.remote.FinanceApiService

class AccountRepositoryImpl(
    private val api: FinanceApiService
) : AccountRepository {

    override suspend fun getAccounts(): List<Account> {
        return api.getAccountList().map { it.toDomain() }
    }

    override suspend fun updateAccounts(accounts: List<Account>) {
        val request = UpdateAccountsRequest(
            accounts = accounts.map { it.toAccountBrief() }
        )
        api.updateAccounts(request)
    }
}
