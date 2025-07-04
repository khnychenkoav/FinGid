package com.example.fingid.domain.repository

import com.example.fingid.domain.model.AccountBriefDomain
import com.example.fingid.domain.model.AccountDomain


interface AccountRepository {
    suspend fun getAccountById(accountId: Int): Result<AccountDomain>
    suspend fun updateAccountById(accountBrief: AccountBriefDomain)
}