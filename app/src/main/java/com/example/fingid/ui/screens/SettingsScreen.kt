package com.example.fingid.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настройки") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            items(settingsItems) { item ->
                SettingRow(item = item)
                HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.5f))
            }
        }
    }
}

@Composable
fun SettingRow(item: SettingItem) {
    var switchState by remember { mutableStateOf(item.isSwitchEnabled) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = item.onClickAction != null || item.hasSwitch) {
                item.onClickAction?.invoke()
            }
            .padding(horizontal = 16.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = item.title,
            style = MaterialTheme.typography.bodyLarge
        )

        if (item.hasSwitch) {
            Switch(
                checked = switchState,
                onCheckedChange = {
                    switchState = it
                    item.onSwitchChange?.invoke(it)
                }
            )
        } else if (item.onClickAction != null) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "Перейти к ${item.title}",
                modifier = Modifier.size(18.dp),
                tint = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    FinGidTheme {
        SettingsScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun SettingRowPreview() {
    FinGidTheme {
        Column {
            SettingRow(SettingItem(title = "Обычный пункт", onClickAction = {}))
            HorizontalDivider()
            SettingRow(SettingItem(title = "Пункт с переключателем", hasSwitch = true, isSwitchEnabled = true, onSwitchChange = {}))
        }
    }
}