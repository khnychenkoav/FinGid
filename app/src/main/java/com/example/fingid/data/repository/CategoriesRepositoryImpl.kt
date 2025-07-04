package com.example.fingid.data.repository

import com.example.fingid.data.datasource.CategoriesRemoteDataSource
import com.example.fingid.data.remote.api.safeApiCall
import com.example.fingid.data.repository.mapper.CategoryDomainMapper
import com.example.fingid.domain.model.CategoryDomain
import com.example.fingid.domain.repository.CategoriesRepository
import javax.inject.Inject


internal class CategoriesRepositoryImpl @Inject constructor(
    private val remoteDataSource: CategoriesRemoteDataSource,
    private val mapper: CategoryDomainMapper
) : CategoriesRepository {


    override suspend fun getCategoriesByType(isIncome: Boolean): Result<List<CategoryDomain>> {
        return safeApiCall {
            remoteDataSource.getCategoriesByType(isIncome)
                .map(mapper::mapCategory)
        }
    }


    override suspend fun getAllCategories(): Result<List<CategoryDomain>> {
        return safeApiCall {
            remoteDataSource.getAllCategories()
                .map(mapper::mapCategory)
        }
    }
}