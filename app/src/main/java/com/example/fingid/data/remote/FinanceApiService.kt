package com.example.fingid.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.example.fingid.data.model.account.AccountDto
import com.example.fingid.data.model.account.AccountHistoryResponse
import com.example.fingid.data.model.category.CategoryDto
import com.example.fingid.data.model.transaction.TransactionDto
import com.example.fingid.data.model.account.UpdateAccountsRequest
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface FinanceApiService {

    @GET("accounts")
    suspend fun getAccountList(): List<AccountDto>

    @GET("accounts/{id}/history")
    suspend fun getAccountHistoryById(@Path("id") id: Int): List<AccountHistoryResponse>

    @POST("accounts")
    suspend fun updateAccounts(@Body request: UpdateAccountsRequest): Response<Unit>

    @GET("categories")
    suspend fun getCategories(): List<CategoryDto>

    @GET("transactions/account/{accountId}/period")
    suspend fun getTransactionsByAccountAndPeriod(
        @Path("accountId") accountId: Int,
        @Query("startDate") from: String,
        @Query("endDate") to: String
    ): List<TransactionDto>
}

object FinanceApi {

    private const val BASE_URL = "https://shmr-finance.ru/api/v1/"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private var authToken: String? = null

    fun setAuthToken(token: String) {
        authToken = token
    }

    private val authInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val tokenInterceptor = Interceptor { chain ->
        val requestBuilder = chain.request().newBuilder()
        authToken?.let { token ->
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }
        chain.proceed(requestBuilder.build())
    }

    private const val TIMEOUT = 60L

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(tokenInterceptor)
        .addInterceptor(authInterceptor)
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val service: FinanceApiService by lazy {
        retrofit.create(FinanceApiService::class.java)
    }
}
