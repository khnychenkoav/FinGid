package com.example.fingid.data.remote.api

import com.example.fingid.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface FinanceApi {

    @GET("accounts")
    suspend fun getAccounts(): Response<List<AccountDto>>

    @POST("accounts")
    suspend fun createAccount(@Body body: AccountCreateRequestDto): Response<AccountDto>

    @GET("accounts/{id}")
    suspend fun getAccount(@Path("id") id: Long): Response<AccountResponseDto>

    @PUT("accounts/{id}")
    suspend fun updateAccount(
        @Path("id") id: Long,
        @Body body: AccountUpdateRequestDto
    ): Response<AccountDto>

    @DELETE("accounts/{id}")
    suspend fun deleteAccount(@Path("id") id: Long): Response<Unit>

    @GET("accounts/{id}/history")
    suspend fun getAccountHistory(@Path("id") id: Long): Response<AccountHistoryResponseDto>

    @GET("categories")
    suspend fun getCategories(): Response<List<CategoryDto>>

    @GET("categories/type/{isIncome}")
    suspend fun getCategoriesByType(@Path("isIncome") isIncome: Boolean): Response<List<CategoryDto>>

    @POST("transactions")
    suspend fun createTransaction(@Body body: TransactionRequestDto): Response<TransactionResponseDto>

    @GET("transactions/{id}")
    suspend fun getTransaction(@Path("id") id: Long): Response<TransactionResponseDto>

    @PUT("transactions/{id}")
    suspend fun updateTransaction(
        @Path("id") id: Long,
        @Body body: TransactionRequestDto
    ): Response<TransactionResponseDto>

    @DELETE("transactions/{id}")
    suspend fun deleteTransaction(@Path("id") id: Long): Response<Unit>

    @GET("transactions/account/{accountId}/period")
    suspend fun getTransactionsByAccountAndPeriod(
        @Path("accountId") accountId: Long,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): Response<List<TransactionResponseDto>>
}
