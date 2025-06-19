package com.example.fingid.data.repository

import com.example.fingid.data.remote.NetworkModule
import com.example.fingid.data.remote.api.FinanceApi
import com.example.fingid.data.remote.dto.*
import com.example.fingid.data.remote.mapper.toDomain
import com.example.fingid.domain.entities.*
import retrofit2.Response

sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(val message: String, val code: Int? = null) : NetworkResult<Nothing>()
}

class FinanceRepository(private val api: FinanceApi = NetworkModule.financeApi) {

    private suspend inline fun <T, reified R> safeApiCall(
        apiCall: suspend () -> Response<T>,
        transform: (T) -> R
    ): NetworkResult<R> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    NetworkResult.Success(transform(body))
                } else {
                    if (response.code() == 204 && Unit is R) {
                        NetworkResult.Success(Unit as R)
                    } else {
                        NetworkResult.Error("Response body is null, code: ${response.code()}", response.code())
                    }
                }
            } else {
                NetworkResult.Error("API call failed: ${response.message()}", response.code())
            }
        } catch (e: Exception) {
            NetworkResult.Error("Network error: ${e.message ?: "Unknown error"}")
        }
    }
    private suspend fun <T> safeApiCallUnit(
        apiCall: suspend () -> Response<T>
    ): NetworkResult<Unit> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {
                NetworkResult.Error("API call failed: ${response.message()}", response.code())
            }
        } catch (e: Exception) {
            NetworkResult.Error("Network error: ${e.message ?: "Unknown error"}")
        }
    }


    suspend fun getAccounts(): NetworkResult<List<Account>> =
        safeApiCall(apiCall = { api.getAccounts() }, transform = { dtoList -> dtoList.map { it.toDomain() } })

    suspend fun createAccount(request: AccountCreateRequestDto): NetworkResult<Account> =
        safeApiCall(apiCall = { api.createAccount(request) }, transform = { it.toDomain() })

    suspend fun getAccount(id: Long): NetworkResult<Account> =
        safeApiCall(apiCall = { api.getAccount(id) }, transform = { it.toDomain() })


    suspend fun updateAccount(id: Long, request: AccountUpdateRequestDto): NetworkResult<Account> =
        safeApiCall(apiCall = { api.updateAccount(id, request) }, transform = { it.toDomain() })

    suspend fun deleteAccount(id: Long): NetworkResult<Unit> =
        safeApiCallUnit { api.deleteAccount(id) }


    suspend fun getAccountHistory(id: Long): NetworkResult<AccountHistoryResponseDto> =
        safeApiCall(apiCall = { api.getAccountHistory(id) }, transform = { it }) // TODO: Map to domain if needed

    suspend fun getCategories(): NetworkResult<List<Category>> =
        safeApiCall(apiCall = { api.getCategories() }, transform = { dtoList -> dtoList.map { it.toDomain() } })

    suspend fun getCategoriesByType(isIncome: Boolean): NetworkResult<List<Category>> =
        safeApiCall(apiCall = { api.getCategoriesByType(isIncome) }, transform = { dtoList -> dtoList.map { it.toDomain() } })

    suspend fun createTransaction(request: TransactionRequestDto): NetworkResult<Transaction> =
        safeApiCall(apiCall = { api.createTransaction(request) }, transform = { it.toDomain() })


    suspend fun getTransaction(id: Long): NetworkResult<Transaction> =
        safeApiCall(apiCall = { api.getTransaction(id) }, transform = { it.toDomain() })

    suspend fun updateTransaction(id: Long, request: TransactionRequestDto): NetworkResult<Transaction> =
        safeApiCall(apiCall = { api.updateTransaction(id, request) }, transform = { it.toDomain() })

    suspend fun deleteTransaction(id: Long): NetworkResult<Unit> =
        safeApiCallUnit { api.deleteTransaction(id) }


    suspend fun getTransactionsByAccountAndPeriod(
        accountId: Long,
        startDate: String?,
        endDate: String?
    ): NetworkResult<List<Transaction>> =
        safeApiCall(
            apiCall = { api.getTransactionsByAccountAndPeriod(accountId, startDate, endDate) },
            transform = { dtoList -> dtoList.map { it.toDomain() } }
        )
}