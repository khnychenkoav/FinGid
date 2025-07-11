package com.example.fingid.presentation.feature.settings.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fingid.R
import com.example.fingid.core.di.daggerViewModel
import com.example.fingid.core.navigation.SettingsListItem
import com.example.fingid.presentation.feature.main.model.ScreenConfig
import com.example.fingid.presentation.feature.main.model.TopBarConfig
import com.example.fingid.presentation.feature.settings.component.ThemeSwitcherOptionCard
import com.example.fingid.presentation.feature.settings.viewmodel.SettingsScreenViewModel
import com.example.fingid.presentation.shared.components.ListItemCard
import com.example.fingid.presentation.shared.model.ListItem
import com.example.fingid.presentation.shared.model.MainContent

@Composable
fun SettingsScreen(
    viewModel: SettingsScreenViewModel = daggerViewModel(),
    updateConfigState: (ScreenConfig) -> Unit
) {
    val darkThemeStatus by viewModel.darkThemeStatus.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        updateConfigState(
            ScreenConfig(
                topBarConfig = TopBarConfig(titleResId = R.string.settings_screen_title)
            )
        )
    }

    Column(Modifier.fillMaxSize()) {
        LazyColumn {
            item {
                ThemeSwitcherOptionCard(
                    title = stringResource(R.string.dark_theme_option),
                    isChecked = darkThemeStatus,
                    onCheckedChange = { viewModel.switchDarkTheme(it) }
                )
            }
            items(SettingsListItem.items) { option ->
                val optionTitle = stringResource(option.titleResId)
                ListItemCard(
                    modifier = Modifier
                        .clickable { }
                        .height(56.dp),
                    item = ListItem(content = MainContent(title = optionTitle)),
                    trailIcon = R.drawable.ic_filled_arrow_right
                )
            }
        }
    }
}