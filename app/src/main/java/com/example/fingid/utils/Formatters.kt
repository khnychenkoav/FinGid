package com.example.fingid.utils

fun String.formatAsRuble(currencySymbol: String = "₽"): String {
    if (isEmpty()) return ""
    val sign   = if (startsWith('-')) "-" else ""
    val digits = filter { it.isDigit() }

    val sb = StringBuilder(digits)
    var i = sb.length - 3
    while (i > 0) {
        sb.insert(i, ' ')
        i -= 3
    }
    return buildString {
        append(sign)
        append(sb)
        append(" $currencySymbol")
    }
}