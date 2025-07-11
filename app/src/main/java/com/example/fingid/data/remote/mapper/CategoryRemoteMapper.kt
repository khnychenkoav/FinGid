package com.example.fingid.data.remote.mapper

import com.example.fingid.data.model.CategoryDTO
import com.example.fingid.data.remote.model.Category
import dagger.Reusable
import javax.inject.Inject


@Reusable
internal class CategoryRemoteMapper @Inject constructor() {
    fun mapCategory(response: Category): CategoryDTO {
        return CategoryDTO(
            id = response.id,
            name = response.name,
            emoji = response.emoji,
            isIncome = response.isIncome,
        )
    }
}