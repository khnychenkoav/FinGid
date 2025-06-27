package com.example.fingid.domain.usecase.account

import com.example.fingid.domain.model.Account
import com.example.fingid.domain.repository.AccountRepository

class UpdateAccountsUseCase(private val accountRepository: AccountRepository) {
    suspend operator fun invoke(accounts: List<Account>) {
        accountRepository.updateAccounts(accounts)
    }
}
