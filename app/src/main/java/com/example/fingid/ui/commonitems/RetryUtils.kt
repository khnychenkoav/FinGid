package com.example.fingid.ui.commonitems

import retrofit2.HttpException
import kotlinx.coroutines.delay
import java.io.IOException

suspend fun <T> retryWithBackoff(
    retries: Int = DEFAULT_RETRIES,
    delayMillis: Long = DEFAULT_DELAY_MILLIS,
    block: suspend () -> T
): T {
    repeat(retries - 1) {
        try {
            return block()
        } catch (e: HttpException) {
            if (e.code() != FIFEHUNDRED_CODE && e.code() != FOURHUNDRED_x_CODE) throw e
        } catch (_: IOException) {}

        delay(delayMillis)
    }
    return block()
}

private const val DEFAULT_RETRIES = 3
private const val DEFAULT_DELAY_MILLIS = 2000L
private const val FIFEHUNDRED_CODE = 500
private const val FOURHUNDRED_x_CODE = 500