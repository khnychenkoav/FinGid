package com.example.fingid.core.utils

import java.text.DecimalFormat


fun String.formatWithSpaces(): String {
    return try {
        this.toBigDecimal().let { decimal ->
            DecimalFormat("#,###").format(decimal)
                .replace(",", " ")
        }
    } catch (e: Exception) {
        this
    }
}