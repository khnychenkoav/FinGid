package com.example.fingid.presentation.shared.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

data class ListItem(
    val lead: LeadContent? = null,
    val content: MainContent,
    val trail: TrailContent? = null
)

sealed class LeadContent {
    abstract val color: Color?

    data class Text(
        val text: String,
        override val color: Color? = null
    ) : LeadContent()

    data class Icon(
        @DrawableRes val iconResId: Int,
        override val color: Color? = null
    ) : LeadContent()
}

data class MainContent(
    val title: String,
    val subtitle: String? = null,
    val color: Color? = null
)

data class TrailContent(
    val text: String,
    val subtext: String? = null
)