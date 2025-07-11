package com.example.fingid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.fingid.core.di.LocalActivityComponent
import com.example.fingid.presentation.feature.main.screen.MainScreen
import com.example.fingid.presentation.shared.theme.FinGidTheme

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
        setContent {
            CompositionLocalProvider(
                LocalActivity provides this,
                LocalActivityComponent provides activityComponent
            ) {
                FinGidTheme(darkTheme = false, dynamicColor = false) {
                    MainScreen()
                }
            }
        }
    }
}