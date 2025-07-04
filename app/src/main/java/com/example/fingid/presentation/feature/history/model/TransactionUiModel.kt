package com.example.fingid.presentation.feature.history.model

import com.example.fingid.core.utils.formatWithSpaces
import com.example.fingid.presentation.shared.model.LeadContent
import com.example.fingid.presentation.shared.model.ListItem
import com.example.fingid.presentation.shared.model.MainContent
import com.example.fingid.presentation.shared.model.TrailContent

data class TransactionUiModel(
    val id: Int,
    val title: String,
    val amount: Int,
    val currency: String,
    val subtitle: String?,
    val emoji: String,
    val transactionAt: String,
) {
    private val amountFormatted: String
        get() = "${amount.toString().formatWithSpaces()} $currency"

    fun toListItem(): ListItem {
        return ListItem(
            lead = LeadContent.Text(text = emoji),
            content = MainContent(title = title, subtitle = subtitle),
            trail = TrailContent(text = amountFormatted, subtext = transactionAt)
        )
    }
}
