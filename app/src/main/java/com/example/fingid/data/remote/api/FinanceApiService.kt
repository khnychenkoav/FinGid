package com.example.fingid.data.remote.api

import com.example.fingid.data.remote.model.Account
import com.example.fingid.data.remote.model.AccountResponse
import com.example.fingid.data.remote.model.AccountUpdateRequest
import com.example.fingid.data.remote.model.Category
import com.example.fingid.data.remote.model.Transaction
import com.example.fingid.data.remote.model.TransactionRequest
import com.example.fingid.data.remote.model.TransactionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface FinanceApiService {


    @GET("accounts/{id}")
    suspend fun getAccountById(@Path("id") accountId: Int): AccountResponse


    @PUT("accounts/{id}")
    suspend fun updateAccountById(
        @Path("id") accountId: Int,
        @Body request: AccountUpdateRequest
    ): Account


    @GET("transactions/account/{accountId}/period")
    suspend fun getTransactionsByPeriod(
        @Path("accountId") accountId: Int,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): List<TransactionResponse>


    @POST("transactions")
    suspend fun createTransaction(
        @Body request: TransactionRequest
    ): Transaction


    @GET("transactions/{id}")
    suspend fun getTransactionById(@Path("id") id: Int): TransactionResponse


    @PUT("transactions/{id}")
    suspend fun updateTransactionById(
        @Path("id") id: Int,
        @Body request: TransactionRequest
    ): TransactionResponse


    @DELETE("transactions/{id}")
    suspend fun deleteTransactionById(@Path("id") id: Int): Response<Void>


    @GET("categories/type/{isIncome}")
    suspend fun getCategoriesByType(
        @Path("isIncome") isIncome: Boolean
    ): List<Category>


    @GET("categories")
    suspend fun getAllCategories(): List<Category>
}