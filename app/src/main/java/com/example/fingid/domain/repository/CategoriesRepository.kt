package com.example.fingid.domain.repository

import com.example.fingid.domain.model.CategoryDomain


interface CategoriesRepository {
    suspend fun getCategoriesByType(isIncome: Boolean): Result<List<CategoryDomain>>
    suspend fun getAllCategories(): Result<List<CategoryDomain>>
}