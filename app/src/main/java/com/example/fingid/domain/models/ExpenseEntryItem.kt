package com.example.fingid.domain.models

import androidx.compose.ui.graphics.Color

data class ExpenseEntryItem(
    val id: String,
    val categoryName: String,
    val subCategoryName: String? = null,
    val amount: String,
    val displayIcon: String? = null,
    val iconCircleBackgroundColor: Color? = null,
    val isTotal: Boolean = false,
    val showArrow: Boolean = !isTotal,
    val customBackgroundColor: Color? = null
)