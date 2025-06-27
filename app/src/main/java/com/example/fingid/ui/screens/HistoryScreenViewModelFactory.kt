package com.example.fingid.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fingid.data.repository.RepositoryProvider
import com.example.fingid.domain.usecase.account.GetAccountUseCase
import com.example.fingid.domain.usecase.transaction.GetTransactionsUseCase

class HistoryScreenViewModelFactory : ViewModelProvider.Factory {
    private val transactionRepository by lazy { RepositoryProvider.getTransactionRepository() }
    private val accountRepository by lazy { RepositoryProvider.getAccountRepository() }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryScreenViewModel(
                getTransactionsUseCase = GetTransactionsUseCase(transactionRepository),
                getAccountUseCase = GetAccountUseCase(accountRepository)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}