package com.example.fingid.core.di

import com.example.fingid.data.repository.AccountRepositoryImpl
import com.example.fingid.data.repository.CategoriesRepositoryImpl
import com.example.fingid.data.repository.TransactionsRepositoryImpl
import com.example.fingid.domain.repository.AccountRepository
import com.example.fingid.domain.repository.CategoriesRepository
import com.example.fingid.domain.repository.TransactionsRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton


@Module
internal interface RepositoryModule {


    @Binds
    @Singleton
    fun bindAccountRepository(
        accountRepository: AccountRepositoryImpl,
    ): AccountRepository


    @Binds
    @Singleton
    fun bindCategoriesRepository(
        categoriesRepository: CategoriesRepositoryImpl,
    ): CategoriesRepository


    @Binds
    @Singleton
    fun bindTransactionsRepository(
        transactionsRepository: TransactionsRepositoryImpl,
    ): TransactionsRepository
}