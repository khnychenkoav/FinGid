package com.example.fingid.domain.model

import com.example.fingid.R

data class ScaffoldItem(
    val leadingImageResId: Int? = R.drawable.refresh,
    val trailingImageResId: Int? = R.drawable.refresh,
    val textResId: Int = R.string.expenses_today,
)
