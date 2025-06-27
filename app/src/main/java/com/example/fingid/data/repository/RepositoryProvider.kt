package com.example.fingid.data.repository

import com.example.fingid.data.remote.FinanceApi
import com.example.fingid.domain.repository.AccountRepository
import com.example.fingid.domain.repository.CategoryRepository
import com.example.fingid.domain.repository.TransactionRepository


object RepositoryProvider {

    private lateinit var accountRepository: AccountRepository
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var transactionRepository: TransactionRepository

    fun initialize() {
        accountRepository = AccountRepositoryImpl(FinanceApi.service)
        categoryRepository = CategoryRepositoryImpl(FinanceApi.service)
        transactionRepository = TransactionRepositoryImpl(FinanceApi.service)
    }

    fun getAccountRepository(): AccountRepository {
        check(::accountRepository.isInitialized) { "AccountRepository is not initialized. Call initialize() first." }
        return accountRepository
    }

    fun getCategoryRepository(): CategoryRepository {
        check(::categoryRepository.isInitialized) {
            throw IllegalStateException("CategoryRepository is not initialized. Call initialize() first.")
        }
        return categoryRepository
    }

    fun getTransactionRepository(): TransactionRepository {
        check(::transactionRepository.isInitialized) {
            throw IllegalStateException("TransactionRepository is not initialized. Call initialize() first.")
        }
        return transactionRepository
    }
}
