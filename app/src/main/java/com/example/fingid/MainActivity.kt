package com.example.fingid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.fingid.core.di.LocalActivityComponent
import com.example.fingid.data.local.SettingsManager
import com.example.fingid.presentation.feature.main.screen.MainScreen
import com.example.fingid.presentation.shared.theme.FinGidTheme
import kotlinx.coroutines.flow.MutableStateFlow

val darkThemeState = MutableStateFlow(false)
class MainActivity : ComponentActivity() {
    private val activityComponent by lazy {
        (application as App).appComponent
            .activityComponent()
            .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        val settingsManager = SettingsManager(applicationContext)
        darkThemeState.value = settingsManager.isDarkThemeEnabled()

        setContent {
            CompositionLocalProvider(
                LocalActivity provides this,
                LocalActivityComponent provides activityComponent
            ) {
                val isDarkTheme by darkThemeState.collectAsState()
                FinGidTheme(darkTheme = false, dynamicColor = false) {
                    MainScreen()
                }
            }
        }
    }
}