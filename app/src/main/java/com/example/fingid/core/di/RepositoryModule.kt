package com.example.fingid.core.di

import com.example.fingid.data.repository.AccountRepositoryImpl
import com.example.fingid.data.repository.CategoriesRepositoryImpl
import com.example.fingid.data.repository.TransactionsRepositoryImpl
import com.example.fingid.domain.repository.AccountRepository
import com.example.fingid.domain.repository.CategoriesRepository
import com.example.fingid.domain.repository.TransactionsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {


    @Binds
    @Singleton
    fun bindsAccountRepository(
        accountRepository: AccountRepositoryImpl,
    ): AccountRepository


    @Binds
    @Singleton
    fun bindsCategoriesRepository(
        categoriesRepository: CategoriesRepositoryImpl,
    ): CategoriesRepository


    @Binds
    @Singleton
    fun bindsTransactionsRepository(
        transactionsRepository: TransactionsRepositoryImpl,
    ): TransactionsRepository
}