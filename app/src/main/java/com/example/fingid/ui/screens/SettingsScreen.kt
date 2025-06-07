package com.example.fingid.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fingid.domain.models.SettingItem
import com.example.fingid.ui.theme.FinGidTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val settingsItems = remember {
        listOf(
            SettingItem(title = "Светлая темная авто", hasSwitch = true, isSwitchEnabled = false, onSwitchChange = {}),
            SettingItem(title = "Основной цвет", onClickAction = {}),
            SettingItem(title = "Звуки", onClickAction = {}),
            SettingItem(title = "Хаптики", onClickAction = {}),
            SettingItem(title = "Код пароль", onClickAction = {}),
            SettingItem(title = "Синхронизация", onClickAction = {}),
            SettingItem(title = "Язык", onClickAction = {}),
            SettingItem(title = "О программе", onClickAction = {})
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {

        Scaffold(
            containerColor = Color.Transparent,

            topBar = {
                TopAppBar(
                    title = { Text("Настройки") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    ),

                    modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal))

                )
            },

        ) { scaffoldPaddingValues ->


            LazyColumn(
                modifier = Modifier
                    .padding(scaffoldPaddingValues)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                items(settingsItems) { item ->
                    SettingRow(item = item)
                    HorizontalDivider(
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }
    }
}

@Composable
fun SettingRow(item: SettingItem) {

}

@Preview(showBackground = true, name = "Settings Screen Preview")
@Composable
fun SettingsScreenPreview() {
    FinGidTheme {
        SettingsScreen()
    }
}

@Preview(showBackground = true, name = "Setting Row Preview")
@Composable
fun SettingRowPreview() {
    FinGidTheme {
        Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            SettingRow(SettingItem(title = "Обычный пункт", onClickAction = {}))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            SettingRow(SettingItem(title = "Пункт с переключателем", hasSwitch = true, isSwitchEnabled = true, onSwitchChange = {}))
        }
    }
}