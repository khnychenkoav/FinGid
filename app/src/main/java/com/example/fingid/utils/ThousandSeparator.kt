package com.example.fingid.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

private fun formatDigits(digits: String): String {
    if (digits.isEmpty()) return ""
    val sb = StringBuilder(digits)
    var i = sb.length - 3
    while (i > 0) {
        sb.insert(i, ' ')
        i -= 3
    }
    return sb.toString()
}


class ThousandsRubleTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val raw       = text.text
        val hasSign   = raw.startsWith('-')
        val signLen   = if (hasSign) 1 else 0
        val digitsRaw = raw.filter { it.isDigit() }
        val digitsFmt = formatDigits(digitsRaw)
        val formatted = buildString {
            if (hasSign) append('-')
            append(digitsFmt)
            if (digitsFmt.isNotEmpty()) append(' ').append('₽')
        }

        val originalToTrans = IntArray(raw.length + 1)
        val transToOriginal = IntArray(formatted.length + 1)

        var o = 0
        var t = 0

        if (hasSign) {
            originalToTrans[o] = t
            transToOriginal[t] = o
            o++; t++
        }

        var digitsSeen = 0
        digitsRaw.forEachIndexed { idx, ch ->
            val needSpace = (digitsRaw.length - idx) % 3 == 0 && idx != 0
            if (needSpace) {
                transToOriginal[t] = o
                t++
            }
            originalToTrans[o] = t
            transToOriginal[t] = o
            t++; o++; digitsSeen++
        }

        originalToTrans[o] = t

        if (digitsFmt.isNotEmpty()) {
            transToOriginal[t]   = o; t++
            transToOriginal[t]   = o; t++
        }
        transToOriginal[t] = o

        val mapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int) = originalToTrans[offset]
            override fun transformedToOriginal(offset: Int) = transToOriginal[offset]
        }

        return TransformedText(AnnotatedString(formatted), mapping)
    }
}
