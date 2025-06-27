package com.example.fingid.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fingid.R
import com.example.fingid.domain.model.Settings
import com.example.fingid.ui.theme.Black
import com.example.fingid.ui.theme.DividerColor
import com.example.fingid.ui.theme.FinGidTheme
import com.example.fingid.ui.theme.SwitchColor
import com.example.fingid.ui.theme.SwitchTrackColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val settingsItems = remember {
        getSettingsList()
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Настройки", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            item {
                DarkModeRow()
                HorizontalDivider(color = DividerColor, thickness = 1.dp)
            }
            items(settingsItems) { item ->
                SettingRow(item = item)
                HorizontalDivider(color = DividerColor, thickness = 1.dp)
            }
        }
    }
}

@Composable
fun DarkModeRow() {
    var checked by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(text = "Темная тема", style = MaterialTheme.typography.bodyLarge, color = Black)
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = checked,
            onCheckedChange = { checked = it },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = SwitchColor,
                checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                uncheckedTrackColor = SwitchTrackColor
            )
        )
    }
}

@Composable
fun SettingRow(item: Settings) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { /* TODO: Handle click */ }
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(text = stringResource(id = item.textLeadingResId), style = MaterialTheme.typography.bodyLarge, color = Black)
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            painter = painterResource(id = item.iconTrailingResId),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
    }
}

private fun getSettingsList(): List<Settings> = listOf(
    Settings(0, R.string.main_color, R.drawable.ic_more_vert),
    Settings(1, R.string.sounds, R.drawable.ic_more_vert),
    Settings(2, R.string.haptics, R.drawable.ic_more_vert),
    Settings(3, R.string.password, R.drawable.ic_more_vert),
    Settings(4, R.string.synchronizing, R.drawable.ic_more_vert),
    Settings(5, R.string.language, R.drawable.ic_more_vert),
    Settings(6, R.string.about_the_program, R.drawable.ic_more_vert),
)

@Composable
@Preview(showBackground = true)
fun SettingsScreenPreview() {
    FinGidTheme(darkTheme = false) {
        SettingsScreen()
    }
}