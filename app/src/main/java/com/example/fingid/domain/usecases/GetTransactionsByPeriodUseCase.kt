package com.example.fingid.domain.usecases

import com.example.fingid.data.remote.api.AppError
import com.example.fingid.data.remote.api.NetworkChecker
import com.example.fingid.domain.model.TransactionDomain
import com.example.fingid.domain.repository.TransactionsRepository
import dagger.Reusable
import javax.inject.Inject


@Reusable
class GetTransactionsByPeriodUseCase @Inject constructor(
    private val repository: TransactionsRepository,
    private val networkChecker: NetworkChecker
) {

    suspend operator fun invoke(
        accountId: Int,
        startDate: String? = null,
        endDate: String? = null
    ): Result<List<TransactionDomain>> {
        if (!networkChecker.isNetworkAvailable()) {
            return Result.failure(AppError.Network)
        }

        return repository.getTransactionsByPeriod(accountId, startDate, endDate)
            .map { list ->
                list.sortedByDescending { it.transactionDate }
            }
    }
}