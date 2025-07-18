package com.example.fingid.data.local.mappers.category

import com.example.fingid.data.local.model.CategoryEntity
import com.example.fingid.domain.model.CategoryDomain

fun CategoryEntity.toCategoryModel(): CategoryDomain = CategoryDomain(
    id = id,
    name = name,
    emoji = emoji,
    isIncome = isIncome
)