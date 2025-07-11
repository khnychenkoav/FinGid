package com.example.fingid.core.di

import androidx.activity.ComponentActivity
import androidx.compose.runtime.staticCompositionLocalOf


val LocalActivity = staticCompositionLocalOf<ComponentActivity> {
    error("No Activity provided!")
}


val LocalActivityComponent = staticCompositionLocalOf<ActivityComponent> {
    error("No ActivityComponent provided!")
}