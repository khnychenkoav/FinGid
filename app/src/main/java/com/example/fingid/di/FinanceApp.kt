package com.example.fingid.di

import android.app.Application
import com.example.fingid.BuildConfig
import com.example.fingid.data.remote.FinanceApi
import com.example.fingid.data.repository.RepositoryProvider

class FinanceApp : Application() {
    override fun onCreate() {
        super.onCreate()
        RepositoryProvider.initialize()
        FinanceApi.setAuthToken(BuildConfig.FINANCE_API_KEY)
    }
}
