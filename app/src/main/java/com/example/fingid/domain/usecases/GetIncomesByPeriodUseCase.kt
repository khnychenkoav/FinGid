package com.example.fingid.domain.usecases

import com.example.fingid.domain.model.TransactionResponseDomain
import dagger.Reusable
import javax.inject.Inject


@Reusable
class GetIncomesByPeriodUseCase @Inject constructor(
    private val getTransactionsByPeriod: GetTransactionsByPeriodUseCase
) {

    suspend operator fun invoke(
        accountId: Int,
        startDate: String? = null,
        endDate: String? = null
    ): Result<List<TransactionResponseDomain>> {
        return getTransactionsByPeriod(accountId, startDate, endDate)
            .map { list ->
                list.filter { it.category.isIncome }
            }
    }
}