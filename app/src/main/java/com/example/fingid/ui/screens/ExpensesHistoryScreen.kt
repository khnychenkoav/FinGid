package com.example.fingid.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import com.example.fingid.R
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fingid.domain.models.ExpenseEntryItem
import com.example.fingid.navigation.Screen
import com.example.fingid.ui.theme.*
import com.example.fingid.utils.formatAsRuble
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private fun Long.toLocalDate(): LocalDate =
    Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesHistoryScreen(navController: NavController) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    var startDate by remember { mutableStateOf(LocalDate.now().withDayOfMonth(1)) }
    var endDate   by remember { mutableStateOf(LocalDate.now()) }
    var showPickerForStart by remember { mutableStateOf(false) }
    var showPickerForEnd   by remember { mutableStateOf(false) }
    val headers = listOf(
        "Начало" to startDate.format(dateFormatter),
        "Конец"  to endDate.format(dateFormatter),
        "Сумма"  to "125 868 ₽"
    )

    val history = remember { sampleHistory() }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                title = { Text("Моя история", style = MaterialTheme.typography.titleLarge) },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(
                            Screen.Analysis.createRoute(
                                startDate.format(dateFormatter),
                                endDate.format(dateFormatter)
                            )
                        )
                    }) {
                        Icon(painter = painterResource(R.drawable.ic_trailng_clock), contentDescription = "Фильтр по периоду", tint = LightGrey, modifier = Modifier.size(48.dp))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { pv ->
        LazyColumn(
            modifier = Modifier
                .padding(pv)
                .fillMaxSize()
        ) {
            items(headers) { (label, value) ->
                InfoRow(label = label, value = value, onClick = {
                    when (label) {
                        "Начало" -> showPickerForStart = true
                        "Конец"  -> showPickerForEnd   = true
                    }
                })
                if (label != headers.last().first) {
                    HorizontalDivider(color = DividerColor, thickness = 1.dp)
                }
            }

            items(history, key = { it.id }) { entry ->
                HistoryEntryRow(item = entry, onClick = {
                    // TODO: переход к деталям операции / редактированию
                    println("Clicked history item ${'$'}{entry.id}")
                })
                HorizontalDivider(color = DividerColor, thickness = 1.dp)
            }
        }
        if (showPickerForStart) {
            val state = rememberDatePickerState(initialSelectedDateMillis = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli())
            DatePickerDialog(
                onDismissRequest = { showPickerForStart = false },
                confirmButton = {
                    TextButton(onClick = {
                        state.selectedDateMillis?.let { startDate = it.toLocalDate() }
                        showPickerForStart = false
                    }) { Text("OK") }
                }
            ) { DatePicker(state = state) }
        }

        if (showPickerForEnd) {
            val state = rememberDatePickerState(initialSelectedDateMillis = endDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli())
            DatePickerDialog(
                onDismissRequest = { showPickerForEnd = false },
                confirmButton = {
                    TextButton(onClick = {
                        state.selectedDateMillis?.let { endDate = it.toLocalDate() }
                        showPickerForEnd = false
                    }) { Text("OK") }
                }
            ) { DatePicker(state = state) }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightGreen)
            .clickable { onClick() }
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = Black,
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.End,
            color = Black,
            modifier = Modifier.weight(0.6f)
        )
    }
}

@Composable
private fun HistoryEntryRow(item: ExpenseEntryItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(EditProfileBackgroundColor)
            .clickable { onClick() }
            .height(70.dp)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(item.iconCircleBackgroundColor ?: MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            val displayText = if (!item.displayIcon.isNullOrEmpty()) {
                item.displayIcon
            } else {
                item.categoryName
                    .split(' ')
                    .filter { it.isNotBlank() }
                    .take(2)
                    .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                    .joinToString("")
            }
            Text(
                text = displayText,
                style = TextStyle(fontSize = if (isEmoji(displayText)) 14.sp else 10.sp, fontWeight = FontWeight.Medium),
                color = Black,
                maxLines = 1
            )
        }
        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(item.categoryName, style = MaterialTheme.typography.bodyLarge, color = Black)
            if (!item.subCategoryName.isNullOrBlank()) {
                Text(item.subCategoryName, style = MaterialTheme.typography.bodySmall, color = Black)
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(horizontalAlignment = Alignment.End) {
            Text(item.amount, style = MaterialTheme.typography.bodyLarge, color = Black)
            Spacer(modifier = Modifier.height(2.dp))
            Text("22:01", style = MaterialTheme.typography.bodySmall, color = Black)
        }

        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_more_vert),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            modifier = Modifier.size(20.dp)
        )
    }
}

private fun sampleHistory(): List<ExpenseEntryItem> {
    val iconBg = LightGreen
    return buildList {
        add(
            ExpenseEntryItem(
                id = "remont",
                categoryName = "Ремонт квартиры",
                subCategoryName = "Ремонт – фурнитура для дверей",
                amount = "100000".formatAsRuble(),
                displayIcon = "РК",
                iconCircleBackgroundColor = iconBg
            )
        )
        for (i in 1..4) {
            add(
                ExpenseEntryItem(
                    id = "dog_$i",
                    categoryName = "На собачку",
                    amount = "100000".formatAsRuble(),
                    displayIcon = "🐶",
                    iconCircleBackgroundColor = iconBg
                )
            )
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ExpensesHistoryScreenPreview() {
    FinGidTheme(darkTheme = false) {
        ExpensesHistoryScreen(navController = rememberNavController())
    }
}