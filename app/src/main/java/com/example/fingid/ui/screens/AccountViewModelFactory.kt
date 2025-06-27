package com.example.fingid.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fingid.data.repository.RepositoryProvider
import com.example.fingid.domain.usecase.account.GetAccountUseCase

class AccountViewModelFactory(private val accountId: Long) : ViewModelProvider.Factory {
    private val accountRepository = RepositoryProvider.getAccountRepository()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AccountViewModel(
                getAccountUseCase = GetAccountUseCase(accountRepository),
                accountId = accountId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}