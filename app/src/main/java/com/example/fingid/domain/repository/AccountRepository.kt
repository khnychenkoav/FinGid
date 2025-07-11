package com.example.fingid.domain.repository

import com.example.fingid.domain.model.AccountBriefDomain
import com.example.fingid.domain.model.AccountDomain
import com.example.fingid.domain.model.AccountResponseDomain



interface AccountRepository {
    suspend fun getAccountById(accountId: Int): Result<AccountResponseDomain>
    suspend fun updateAccountById(accountBrief: AccountBriefDomain): Result<AccountDomain>
}