package com.example.fingid.data.datasource

import com.example.fingid.data.model.CategoryDTO
import com.example.fingid.data.remote.api.FinanceApiService
import com.example.fingid.data.remote.mapper.CategoryRemoteMapper
import javax.inject.Inject


interface CategoriesRemoteDataSource {
    suspend fun getCategoriesByType(isIncome: Boolean): List<CategoryDTO>
    suspend fun getAllCategories(): List<CategoryDTO>
}


internal class CategoriesRemoteDataSourceImpl @Inject constructor(
    private val api: FinanceApiService,
    private val mapper: CategoryRemoteMapper
) : CategoriesRemoteDataSource {


    override suspend fun getCategoriesByType(isIncome: Boolean): List<CategoryDTO> {
        return api.getCategoriesByType(isIncome)
            .map(mapper::mapCategory)
    }


    override suspend fun getAllCategories(): List<CategoryDTO> {
        return api.getAllCategories()
            .map(mapper::mapCategory)
    }
}