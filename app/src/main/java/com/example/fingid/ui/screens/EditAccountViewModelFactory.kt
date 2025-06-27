package com.example.fingid.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fingid.data.repository.RepositoryProvider
import com.example.fingid.domain.usecase.account.GetAccountUseCase
import com.example.fingid.domain.usecase.account.UpdateAccountsUseCase

class EditAccountViewModelFactory(private val accountId: Long?) : ViewModelProvider.Factory {
    private val accountRepository by lazy { RepositoryProvider.getAccountRepository() }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditAccountViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditAccountViewModel(
                accountIdToEdit = accountId,
                getAccountUseCase = GetAccountUseCase(accountRepository),
                updateAccountsUseCase = UpdateAccountsUseCase(accountRepository)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}