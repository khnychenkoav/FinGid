package com.example.fingid

import android.app.Application
import androidx.work.*
import com.example.fingid.core.di.AppComponent
import com.example.fingid.core.di.AppModule
import com.example.fingid.core.di.DaggerAppComponent
import com.example.fingid.worker.SyncWorker
import java.util.concurrent.TimeUnit

class App : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .appModule(AppModule(this))
            .build()

        scheduleSyncWorker()
    }

    private fun scheduleSyncWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = PeriodicWorkRequestBuilder<SyncWorker>(2, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "sync_transactions",
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )
    }
}