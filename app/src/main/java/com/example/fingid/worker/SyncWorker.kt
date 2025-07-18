package com.example.fingid.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.fingid.App
import com.example.fingid.domain.repository.TransactionsRepository
import javax.inject.Inject

class SyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    @Inject
    lateinit var transactionsRepository: TransactionsRepository

    override suspend fun doWork(): Result {
        (applicationContext as App).appComponent.inject(this)

        return try {
            transactionsRepository.getTransactionsByPeriod(269, null, null)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}