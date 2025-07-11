package com.example.fingid.domain.usecases

import com.example.fingid.data.remote.api.AppError
import com.example.fingid.data.remote.api.NetworkChecker
import com.example.fingid.domain.model.AccountBriefDomain
import com.example.fingid.domain.model.AccountDomain
import com.example.fingid.domain.repository.AccountRepository
import dagger.Reusable
import javax.inject.Inject


@Reusable
class UpdateAccountUseCase @Inject constructor(
    private val repository: AccountRepository,
    private val networkChecker: NetworkChecker
) {

    suspend operator fun invoke(
        accountId: Int,
        accountName: String,
        accountBalance: Int,
        accountCurrency: String
    ): Result<AccountDomain> {
        if (!networkChecker.isNetworkAvailable()) {
            return Result.failure(AppError.Network)
        }

        return repository.updateAccountById(
            AccountBriefDomain(
                id = accountId,
                name = accountName,
                balance = accountBalance,
                currency = accountCurrency
            )
        )
    }
}