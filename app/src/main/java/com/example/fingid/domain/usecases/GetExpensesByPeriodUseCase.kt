package com.example.fingid.domain.usecases

import com.example.fingid.domain.model.TransactionDomain
import dagger.Reusable
import javax.inject.Inject


@Reusable
class GetExpensesByPeriodUseCase @Inject constructor(
    private val getTransactionsByPeriod: GetTransactionsByPeriodUseCase
) {

    suspend operator fun invoke(
        accountId: Int,
        startDate: String? = null,
        endDate: String? = null
    ): Result<List<TransactionDomain>> {
        return getTransactionsByPeriod(accountId, startDate, endDate)
            .map { list ->
                list.filterNot { it.category.isIncome }
            }
    }
}