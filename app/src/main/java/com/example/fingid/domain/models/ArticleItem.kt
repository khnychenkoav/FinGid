package com.example.fingid.domain.models

import androidx.compose.ui.graphics.Color

data class ArticleItem(
    val id: String,
    val title: String,
    val emojiOrIconCode: String? = null,
    val iconBackgroundColor: Color
)