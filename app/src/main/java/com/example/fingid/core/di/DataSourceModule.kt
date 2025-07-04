package com.example.fingid.core.di

import com.example.fingid.data.datasource.AccountRemoteDataSource
import com.example.fingid.data.datasource.AccountRemoteDataSourceImpl
import com.example.fingid.data.datasource.CategoriesRemoteDataSource
import com.example.fingid.data.datasource.CategoriesRemoteDataSourceImpl
import com.example.fingid.data.datasource.TransactionsRemoteDataSource
import com.example.fingid.data.datasource.TransactionsRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal interface DataSourceModule {


    @Binds
    @Singleton
    fun bindsAccountRemoteDataSource(
        accountRemoteDataSource: AccountRemoteDataSourceImpl
    ): AccountRemoteDataSource


    @Binds
    @Singleton
    fun bindsCategoriesRemoteDataSource(
        categoriesRemoteDataSource: CategoriesRemoteDataSourceImpl
    ): CategoriesRemoteDataSource


    @Binds
    @Singleton
    fun bindsTransactionsRemoteDataSource(
        transactionsRemoteDataSource: TransactionsRemoteDataSourceImpl
    ): TransactionsRemoteDataSource
}