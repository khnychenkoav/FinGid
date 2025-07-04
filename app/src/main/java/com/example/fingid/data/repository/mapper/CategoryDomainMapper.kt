package com.example.fingid.data.repository.mapper

import com.example.fingid.data.model.CategoryDTO
import com.example.fingid.domain.model.CategoryDomain
import javax.inject.Inject


internal class CategoryDomainMapper @Inject constructor() {
    fun mapCategory(dto: CategoryDTO): CategoryDomain {
        return CategoryDomain(
            id = dto.id,
            name = dto.name,
            emoji = dto.emoji,
            isIncome = dto.isIncome
        )
    }
}