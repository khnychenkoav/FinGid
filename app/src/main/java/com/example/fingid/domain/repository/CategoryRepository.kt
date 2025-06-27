package com.example.fingid.domain.repository

import com.example.fingid.domain.model.Category

interface CategoryRepository {
    suspend fun getCategories(): List<Category>
}
