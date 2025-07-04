package com.example.fingid.presentation.feature.balance_edit.mapper

import com.example.fingid.domain.model.AccountDomain
import com.example.fingid.presentation.feature.balance_edit.model.BalanceDetailedUiModel
import javax.inject.Inject


class AccountToBalanceDetailedMapper @Inject constructor() {
    fun map(domain: AccountDomain): BalanceDetailedUiModel {
        return BalanceDetailedUiModel(
            id = domain.id,
            name = domain.name,
            amount = domain.balance.toString(),
            currencyCode = domain.currency,
            currencySymbol = domain.getCurrencySymbol(),
        )
    }
}