package com.example.fingid.data.remote.api

import com.example.fingid.data.remote.model.AccountResponse
import com.example.fingid.data.remote.model.AccountUpdateRequest
import com.example.fingid.data.remote.model.CategoryResponse
import com.example.fingid.data.remote.model.TransactionResponse
import retrofit2.http.Body
import retrofit2.http.GET
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
    )


    @GET("transactions/account/{accountId}/period")
    suspend fun getTransactionsByPeriod(
        @Path("accountId") accountId: Int,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): List<TransactionResponse>


    @GET("categories/type/{isIncome}")
    suspend fun getCategoriesByType(
        @Path("isIncome") isIncome: Boolean
    ): List<CategoryResponse>


    @GET("categories")
    suspend fun getAllCategories(): List<CategoryResponse>
}