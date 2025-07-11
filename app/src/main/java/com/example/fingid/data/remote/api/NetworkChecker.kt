package com.example.fingid.data.remote.api

import android.content.Context
import android.net.ConnectivityManager
import dagger.Reusable
import javax.inject.Inject


interface NetworkChecker {
    fun isNetworkAvailable(): Boolean
}

@Reusable
class NetworkCheckerImpl @Inject constructor(
    private val context: Context
) : NetworkChecker {

    override fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetworkInfo

        return activeNetwork != null && activeNetwork.isConnected
    }
}