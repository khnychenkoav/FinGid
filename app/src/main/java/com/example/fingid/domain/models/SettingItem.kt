package com.example.fingid.domain.models

data class SettingItem (
    val title: String,
    val hasSwitch: Boolean = false,
    val isSwitchEnabled: Boolean = false,
    val onSwitchChange: ((Boolean) -> Unit)? = null,
    val onClickAction: (() -> Unit)? = null
)