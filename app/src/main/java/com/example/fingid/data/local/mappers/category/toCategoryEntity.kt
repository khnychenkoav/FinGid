package com.example.fingid.data.local.mappers.category

import com.example.fingid.data.local.model.CategoryEntity
import com.example.fingid.domain.model.CategoryDomain

fun CategoryDomain.toCategoryEntity(): CategoryEntity = CategoryEntity(
    id = id,
    name = name,
    emoji = emoji,
    isIncome = isIncome
)