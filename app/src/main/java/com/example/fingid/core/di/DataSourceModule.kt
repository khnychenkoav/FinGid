package com.example.fingid.core.di

import com.example.fingid.data.datasource.AccountRemoteDataSource
import com.example.fingid.data.datasource.AccountRemoteDataSourceImpl
import com.example.fingid.data.datasource.CategoriesRemoteDataSource
import com.example.fingid.data.datasource.CategoriesRemoteDataSourceImpl
import com.example.fingid.data.datasource.TransactionsLocalDataSource
import com.example.fingid.data.datasource.TransactionsLocalDataSourceImpl
import com.example.fingid.data.datasource.TransactionsRemoteDataSource
import com.example.fingid.data.datasource.TransactionsRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton


@Module
internal interface DataSourceModule {


    @Binds
    @Singleton
    fun bindAccountRemoteDataSource(
        accountRemoteDataSource: AccountRemoteDataSourceImpl
    ): AccountRemoteDataSource


    @Binds
    @Singleton
    fun bindCategoriesRemoteDataSource(
        categoriesRemoteDataSource: CategoriesRemoteDataSourceImpl
    ): CategoriesRemoteDataSource


    @Binds
    @Singleton
    fun bindTransactionsRemoteDataSource(
        transactionsRemoteDataSource: TransactionsRemoteDataSourceImpl
    ): TransactionsRemoteDataSource


    @Binds
    @Singleton
    fun bindTransactionsLocalDataSource(
        transactionsLocalDataSource: TransactionsLocalDataSourceImpl
    ): TransactionsLocalDataSource
}