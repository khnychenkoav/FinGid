package com.example.fingid.presentation.feature.transaction_creation.mapper

import com.example.fingid.domain.model.AccountResponseDomain
import com.example.fingid.presentation.feature.transaction_creation.model.BalanceBriefUiModel
import javax.inject.Inject


class AccountToBalanceBriefMapper @Inject constructor() {
    fun map(domain: AccountResponseDomain): BalanceBriefUiModel {
        return BalanceBriefUiModel(
            id = domain.id,
            name = domain.name,
            currencySymbol = domain.getCurrencySymbol()
        )
    }
}