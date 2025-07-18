package com.example.fingid.data.local.sync

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSyncStorage @Inject constructor(
    context: Context
) {

    companion object {
        private const val SHARED_PREFS = "sync_prefs"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

    fun saveSyncTime(
        feature: String,
        timestamp: Long
    ) {
        prefs.edit {
            putLong(feature, timestamp)
        }
    }

    fun getSyncTime(
        feature: String
    ): Long {
        return prefs.getLong(feature, 0L)
    }
}