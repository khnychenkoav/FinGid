package com.example.fingid

import android.app.Application
import com.example.fingid.core.di.AppComponent
import com.example.fingid.core.di.AppModule
import com.example.fingid.core.di.DaggerAppComponent


class App : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .appModule(AppModule(this))
            .build()
    }
}