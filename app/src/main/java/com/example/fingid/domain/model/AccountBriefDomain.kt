package com.example.fingid.domain.model

data class AccountBriefDomain(
    val id: Int,
    val name: String,
    val balance: Int,
    val currency: String
) {
    fun getCurrencySymbol(): String {
        return when (currency) {
            "RUB" -> "₽"
            "USD" -> "$"
            "EUR" -> "€"
            else -> currency
        }
    }
}