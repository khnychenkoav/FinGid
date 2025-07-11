package com.example.fingid.presentation.feature.categories.model

import com.example.fingid.domain.model.CategoryDomain
import com.example.fingid.presentation.feature.categories.model.CategoryUiModel
import javax.inject.Inject

class CategoryToCategoryUiMapper @Inject constructor() {
    fun map(domain: CategoryDomain): CategoryUiModel {
        return CategoryUiModel(
            id = domain.id,
            name = domain.name,
            emoji = domain.emoji
        )
    }
}