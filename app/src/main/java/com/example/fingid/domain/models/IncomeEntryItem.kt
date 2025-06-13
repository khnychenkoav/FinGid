package com.example.fingid.domain.models

import androidx.compose.ui.graphics.Color

data class IncomeEntryItem(
    val id: String,
    val categoryName: String,
    val amount: String,
    val isTotal: Boolean = false,
    val showArrow: Boolean = !isTotal,
    val customBackgroundColor: Color? = null
)