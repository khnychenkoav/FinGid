package com.example.fingid.presentation.feature.balance.mapper

import com.example.fingid.domain.model.AccountDomain
import com.example.fingid.presentation.feature.balance.model.BalanceUiModel
import javax.inject.Inject

class AccountToBalanceMapper @Inject constructor() {
    fun map(domain: AccountDomain): BalanceUiModel {
        return BalanceUiModel(
            id = domain.id,
            amount = domain.balance,
            currency = domain.getCurrencySymbol()
        )
    }
}