package com.example.fingid.data.remote.api

import androidx.annotation.StringRes
import com.example.fingid.R

sealed class AppError(@StringRes val messageResId: Int) : Throwable() {
    class ApiError : AppError(R.string.api_error)
    object Network : AppError(R.string.network_error)
    class Unknown : AppError(R.string.unknown_error)
}