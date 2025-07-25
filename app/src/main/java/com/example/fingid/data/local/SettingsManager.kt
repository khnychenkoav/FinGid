package com.example.fingid.data.local

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

private const val PREFS_NAME = "app_settings"
private const val KEY_DARK_THEME = "dark_theme_enabled"

@Singleton
class SettingsManager @Inject constructor(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isDarkThemeEnabled(): Boolean {
        return prefs.getBoolean(KEY_DARK_THEME, false)
    }

    fun setDarkThemeEnabled(isEnabled: Boolean) {
        prefs.edit { putBoolean(KEY_DARK_THEME, isEnabled) }
    }
}