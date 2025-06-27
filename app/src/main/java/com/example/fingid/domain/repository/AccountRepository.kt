package com.example.fingid.domain.repository

import com.example.fingid.domain.model.Account

interface AccountRepository {
    suspend fun getAccounts(): List<Account>
    suspend fun updateAccounts(accounts: List<Account>)
}
