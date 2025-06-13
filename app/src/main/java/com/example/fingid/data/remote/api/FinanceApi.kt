package com.example.fingid.data.remote.api

import com.example.fingid.data.remote.dto.*
import retrofit2.http.*

interface FinanceApi {

    @GET("accounts")
    suspend fun getAccounts(): List<AccountDto>

    @POST("accounts")
    suspend fun createAccount(@Body body: AccountCreateRequestDto): AccountDto

    @GET("accounts/{id}")
    suspend fun getAccount(@Path("id") id: Long): AccountResponseDto

    @PUT("accounts/{id}")
    suspend fun updateAccount(
        @Path("id") id: Long,
        @Body body: AccountUpdateRequestDto
    ): AccountDto

    @GET("accounts/{id}/history")
    suspend fun getAccountHistory(@Path("id") id: Long): AccountHistoryResponseDto

    @GET("categories")
    suspend fun getCategories(): List<CategoryDto>

    @GET("categories/type/{isIncome}")
    suspend fun getCategoriesByType(@Path("isIncome") isIncome: Boolean): List<CategoryDto>

    @POST("transactions")
    suspend fun createTransaction(@Body body: TransactionRequestDto): TransactionResponseDto

    @GET("transactions/{id}")
    suspend fun getTransaction(@Path("id") id: Long): TransactionResponseDto

    @PUT("transactions/{id}")
    suspend fun updateTransaction(
        @Path("id") id: Long,
        @Body body: TransactionRequestDto
    ): TransactionResponseDto

    @DELETE("transactions/{id}")
    suspend fun deleteTransaction(@Path("id") id: Long)

    @GET("transactions/account/{accountId}/period")
    suspend fun getTransactionsByAccountAndPeriod(
        @Path("accountId") accountId: Long,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): List<TransactionResponseDto>
}
