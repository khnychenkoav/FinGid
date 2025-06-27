package com.example.fingid.data.repository

import com.example.fingid.data.mapper.mapToCategory
import com.example.fingid.data.remote.FinanceApiService
import com.example.fingid.domain.model.Category
import com.example.fingid.domain.repository.CategoryRepository


class CategoryRepositoryImpl(
    private val api: FinanceApiService
) : CategoryRepository {

    override suspend fun getCategories(): List<Category> {
        return api.getCategories().map { it.mapToCategory() }
    }
}
