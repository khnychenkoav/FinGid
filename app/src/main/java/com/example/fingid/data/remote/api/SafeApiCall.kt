package com.example.fingid.data.remote.api

import retrofit2.HttpException
import java.io.IOException


suspend fun <T> safeApiCall(
    call: suspend () -> T,
    handleSuccess: (T) -> Result<T> = { Result.success(it) }
): Result<T> {
    return try {
        Result.success(call())
    } catch (e: IOException) {
        Result.failure(AppError.Network)
    } catch (e: HttpException) {
        if (e.code() == 204) {
            @Suppress("UNCHECKED_CAST")
            Result.success(Unit as T)
        } else {
            Result.failure(AppError.ApiError())
        }
    } catch (e: Exception) {
        Result.failure(AppError.Unknown())
    }
}