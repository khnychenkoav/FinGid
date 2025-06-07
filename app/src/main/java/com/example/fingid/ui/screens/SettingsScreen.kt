package com.example.fingid.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Настройки") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
    ) { scaffoldPaddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(scaffoldPaddingValues)
                .fillMaxSize()
        ) {
            items(settingsItems) { item ->
                val listIsOnPrimaryBackground = MaterialTheme.colorScheme.background == MaterialTheme.colorScheme.primary
                SettingRow(item = item, useOnPrimaryColor = listIsOnPrimaryBackground)
                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = if (listIsOnPrimaryBackground) {
                        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)
                    } else {
                        MaterialTheme.colorScheme.outlineVariant
                    }
                )
            }
        }
    }
}

@Composable
fun SettingRow(item: SettingItem, useOnPrimaryColor: Boolean) {
    val textColor = if (useOnPrimaryColor) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
    val iconTint = if (useOnPrimaryColor) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = item.onClickAction != null, onClick = { item.onClickAction?.invoke() })
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = item.title,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor
        )
        if (item.hasSwitch) {
            Switch(
                checked = item.isSwitchEnabled,
                onCheckedChange = item.onSwitchChange
            )
        } else if (item.onClickAction != null) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = item.title,
                tint = iconTint,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "Settings Screen Full Preview")
@Composable
fun SettingsScreenPreview() {
    FinGidTheme(darkTheme = false) {
        SettingsScreen()
    }
}

@Preview(showBackground = true, name = "Setting Row On Default Background Preview")
@Composable
fun SettingRowPreview() {
    FinGidTheme {
        Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            SettingRow(SettingItem(title = "Обычный пункт", onClickAction = {}), useOnPrimaryColor = false)
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            SettingRow(SettingItem(title = "Пункт с переключателем", hasSwitch = true, isSwitchEnabled = true, onSwitchChange = {}), useOnPrimaryColor = false)
        }
    }
}