package com.example.fingid.presentation.feature.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fingid.data.local.SettingsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class SettingsScreenViewModel @Inject constructor(
    private val settingsManager: SettingsManager
) : ViewModel() {

    private val _darkThemeStatus = MutableStateFlow(false)
    val darkThemeStatus: StateFlow<Boolean> = _darkThemeStatus.asStateFlow()

    init {
        viewModelScope.launch {
            _darkThemeStatus.value = settingsManager.isDarkThemeEnabled()
        }
    }

    fun switchDarkTheme(status: Boolean) {
        _darkThemeStatus.value = status
        viewModelScope.launch {
            settingsManager.setDarkThemeEnabled(status)
            com.example.fingid.darkThemeState.value = status
        }
    }
}