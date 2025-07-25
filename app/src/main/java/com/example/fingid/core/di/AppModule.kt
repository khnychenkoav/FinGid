package com.example.fingid.core.di

import android.app.Application
import android.content.Context
import com.example.fingid.data.local.SettingsManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    @Singleton
    fun provideContext(): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideSettingsManager(context: Context): SettingsManager {
        return SettingsManager(context)
    }
}