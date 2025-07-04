package com.example.fingid.domain.usecases

import com.example.fingid.data.remote.api.AppError
import com.example.fingid.data.remote.api.NetworkChecker
import com.example.fingid.domain.model.CategoryDomain
import com.example.fingid.domain.repository.CategoriesRepository
import dagger.Reusable
import javax.inject.Inject


@Reusable
class GetIncomesCategoriesUseCase @Inject constructor(
    private val repository: CategoriesRepository,
    private val networkChecker: NetworkChecker
) {

    suspend operator fun invoke(): Result<List<CategoryDomain>> {
        if (!networkChecker.isNetworkAvailable()) {
            return Result.failure(AppError.Network)
        }

        return repository.getCategoriesByType(true)
    }
}