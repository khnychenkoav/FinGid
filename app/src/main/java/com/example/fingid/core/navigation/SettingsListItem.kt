package com.example.fingid.core.navigation

import androidx.annotation.StringRes
import com.example.fingid.R


data class SettingsListItem(
    @StringRes val titleResId: Int
) {

    companion object {
        val items = listOf(
            SettingsListItem(
                titleResId = R.string.main_color_option,
            ),
            SettingsListItem(
                titleResId = R.string.sounds_options,
            ),
            SettingsListItem(
                titleResId = R.string.haptics_options,
            ),
            SettingsListItem(
                titleResId = R.string.code_password_option,
            ),
            SettingsListItem(
                titleResId = R.string.synchronize_option,
            ),
            SettingsListItem(
                titleResId = R.string.language_option,
            ),
            SettingsListItem(
                titleResId = R.string.about_option,
            )
        )
    }
}