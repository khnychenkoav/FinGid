package com.example.fingid.core.di

import android.content.Context
import com.example.fingid.data.remote.api.FinanceApiService
import com.example.fingid.data.remote.api.NetworkChecker
import com.example.fingid.data.remote.api.NetworkCheckerImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
object NetworkModule {

    private const val BASE_URL = "https://shmr-finance.ru/api/v1/"
    private const val AUTH_TOKEN = "токентокентокен"

    @Provides
    @Named("maxRetries")
    fun provideMaxRetries(): Int = 3

    @Provides
    @Named("retryDelay")
    fun provideRetryDelay(): Long = 2000L

    @Provides
    @Singleton
    fun provideRetryInterceptor(
        @Named("maxRetries") maxRetries: Int,
        @Named("retryDelay") delay: Long
    ): RetryInterceptor {
        return RetryInterceptor(maxRetries, delay)
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(retryInterceptor: RetryInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(retryInterceptor)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $AUTH_TOKEN")
                    .build()
                chain.proceed(request)
            }
            .build()
    }


    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }


    @Provides
    @Singleton
    fun provideNetworkChecker(appContext: Context): NetworkChecker {
        return NetworkCheckerImpl(appContext)
    }


    @Provides
    @Singleton
    fun provideRetrofit(
        moshi: Moshi,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }


    @Provides
    @Singleton
    fun provideFinanceApiService(retrofit: Retrofit): FinanceApiService {
        return retrofit.create(FinanceApiService::class.java)
    }
}