package com.example.fingid.domain.usecases

import com.example.fingid.core.utils.combineDateTimeToIso
import com.example.fingid.data.remote.api.AppError
import com.example.fingid.data.remote.api.NetworkChecker
import com.example.fingid.domain.model.TransactionDomain
import com.example.fingid.domain.model.TransactionResponseDomain
import com.example.fingid.domain.repository.TransactionsRepository
import dagger.Reusable
import javax.inject.Inject


@Reusable
class CreateTransactionUseCase @Inject constructor(
    private val repository: TransactionsRepository,
    private val networkChecker: NetworkChecker
) {
    suspend operator fun invoke(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        transactionTime: String,
        comment: String
    ): Result<TransactionDomain> {
        if (!networkChecker.isNetworkAvailable()) {
            return Result.failure(AppError.Network)
        }

        return repository.createTransaction(
            accountId,
            categoryId,
            amount,
            combineDateTimeToIso(transactionDate, transactionTime),
            comment
        )
    }
}