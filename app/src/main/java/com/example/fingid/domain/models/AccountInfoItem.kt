package com.example.fingid.domain.models

import androidx.compose.ui.graphics.Color
import com.example.fingid.ui.theme.Black

data class AccountInfoItem(
    val id: String,
    val title: String,
    val displayIcon: String? = null,
    val displayIconColor: Color? = Black,
    val iconCircleBackgroundColor: Color? = null,
    val value: String,
    val valueColor: Color? = Black,
    val showArrow: Boolean = true,
    val backgroundColor: Color
)