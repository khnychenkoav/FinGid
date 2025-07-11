package com.example.fingid.presentation.feature.balance_update.mapper

import com.example.fingid.domain.model.AccountResponseDomain
import com.example.fingid.presentation.feature.balance_update.model.BalanceDetailedUiModel
import javax.inject.Inject


class AccountToBalanceDetailedMapper @Inject constructor() {
    fun map(domain: AccountResponseDomain): BalanceDetailedUiModel {
        return BalanceDetailedUiModel(
            id = domain.id,
            name = domain.name,
            amount = domain.balance.toString(),
            currencyCode = domain.currency,
            currencySymbol = domain.getCurrencySymbol(),
        )
    }
}