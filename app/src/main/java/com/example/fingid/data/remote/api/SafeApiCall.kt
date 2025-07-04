package com.example.fingid.data.remote.api

import retrofit2.HttpException
import java.io.IOException


suspend fun <T> safeApiCall(call: suspend () -> T): Result<T> {
    return try {
        Result.success(call())
    } catch (e: IOException) {
        Result.failure(AppError.Network)
    } catch (e: HttpException) {
        Result.failure(AppError.ApiError())
    } catch (e: Exception) {
        Result.failure(AppError.Unknown())
    }
}