package com.example.fingid.presentation.feature.balance_update.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.fingid.R

data class CurrencyItem(
    val currencyCode: String,
    @StringRes val currencyResId: Int,
    @DrawableRes val currencyIconResId: Int,
    val currencySymbol: String
) {
    companion object {
        val items = listOf(
            CurrencyItem(
                currencyCode = "RUB",
                currencyResId = R.string.ruble_currency,
                currencyIconResId = R.drawable.ic_ruble,
                currencySymbol = "₽"
            ),
            CurrencyItem(
                currencyCode = "USD",
                currencyResId = R.string.dollar_currency,
                currencyIconResId = R.drawable.ic_dollar,
                currencySymbol = "$"
            ),
            CurrencyItem(
                currencyCode = "EUR",
                currencyResId = R.string.euro_currency,
                currencyIconResId = R.drawable.ic_euro,
                currencySymbol = "€"
            )
        )
    }
}