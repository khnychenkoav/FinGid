package com.example.fingid.core.di

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Named

const val SERVER_ERROR = 500


class RetryInterceptor @Inject constructor(
    @Named("maxRetries") private val maxRetries: Int,
    @Named("retryDelay") private val retryDelayMillis: Long
) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)

        var tryCount = 0
        while (!response.isSuccessful && response.code() == SERVER_ERROR && tryCount < maxRetries) {
            tryCount++
            response.close()

            runBlocking {
                delay(retryDelayMillis)
            }

            response = chain.proceed(request)
        }

        return response
    }
}