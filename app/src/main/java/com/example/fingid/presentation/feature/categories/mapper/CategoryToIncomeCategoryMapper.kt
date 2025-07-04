package com.example.fingid.presentation.feature.categories.mapper

import com.example.fingid.domain.model.CategoryDomain
import com.example.fingid.presentation.feature.categories.model.IncomeCategoryUiModel
import javax.inject.Inject


class CategoryToIncomeCategoryMapper @Inject constructor() {
    fun map(domain: CategoryDomain): IncomeCategoryUiModel {
        return IncomeCategoryUiModel(
            id = domain.id,
            name = domain.name,
            emoji = domain.emoji
        )
    }
}