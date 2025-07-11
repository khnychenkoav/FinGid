package com.example.fingid.domain.usecases

import com.example.fingid.data.remote.api.AppError
import com.example.fingid.data.remote.api.NetworkChecker
import com.example.fingid.domain.repository.TransactionsRepository
import dagger.Reusable
import javax.inject.Inject


@Reusable
class DeleteTransactionUseCase @Inject constructor(
    private val repository: TransactionsRepository,
    private val networkChecker: NetworkChecker
) {

    suspend operator fun invoke(transactionId: Int): Result<Unit> {
        if (!networkChecker.isNetworkAvailable()) {
            return Result.failure(AppError.Network)
        }

        return repository.deleteTransactionById(transactionId)
    }
}