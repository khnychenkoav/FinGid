package com.example.fingid.presentation.feature.settings.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


class SettingsScreenViewModel @Inject constructor() : ViewModel() {

    private val _darkThemeStatus = MutableStateFlow(false)
    val darkThemeStatus: StateFlow<Boolean> = _darkThemeStatus

    fun switchDarkTheme(status: Boolean) {
        _darkThemeStatus.value = status
    }
}