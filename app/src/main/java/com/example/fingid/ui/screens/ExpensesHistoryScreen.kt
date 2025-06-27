package com.example.fingid.ui.screens

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fingid.R
import com.example.fingid.domain.model.Transaction
import com.example.fingid.navigation.Screen
import com.example.fingid.ui.commonitems.UiState
import com.example.fingid.ui.theme.*
import com.example.fingid.utils.formatAsRuble
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesHistoryScreen(navController: NavController, isIncome: Boolean = false) {
    val context = LocalContext.current
    val viewModel: HistoryScreenViewModel = viewModel(factory = HistoryScreenViewModelFactory())

    val displayDateFormatter = remember { DateTimeFormatter.ofPattern("dd.MM.yyyy") }
    val backendDateFormatter = remember { DateTimeFormatter.ofPattern("yyyy-MM-dd") }

    var fromDate by remember { mutableStateOf(LocalDate.now().withDayOfMonth(1)) }
    var toDate by remember { mutableStateOf(LocalDate.now()) }

    LaunchedEffect(fromDate, toDate, isIncome) {
        viewModel.loadHistory(
            from = fromDate.format(backendDateFormatter),
            to = toDate.format(backendDateFormatter),
            isIncome = isIncome,
            context = context
        )
    }

    val historyState by viewModel.historyList.observeAsState(initial = UiState.Loading)

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
                                fromDate.format(displayDateFormatter),
                                toDate.format(displayDateFormatter)
                            )
                        )
                    }) {
                        Icon(painter = painterResource(R.drawable.ic_trailng_clock), contentDescription = "Анализ", tint = LightGrey)
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
        Column(modifier = Modifier.padding(pv).fillMaxSize()) {
            DateSelectionHeader(
                fromDate = fromDate,
                toDate = toDate,
                onFromDateChanged = { fromDate = it },
                onToDateChanged = { toDate = it },
                totalAmount = (historyState as? UiState.Success)?.data?.sumOf { it.amount }
            )
            HorizontalDivider(color = DividerColor, thickness = 1.dp)

            when (val state = historyState) {
                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is UiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = state.message ?: "Произошла ошибка")
                    }
                }
                is UiState.Success -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.data, key = { it.id }) { transaction ->
                            HistoryEntryRow(item = transaction, onClick = {
                                // TODO: Handle click
                            })
                            HorizontalDivider(color = DividerColor, thickness = 1.dp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DateSelectionHeader(
    fromDate: LocalDate,
    toDate: LocalDate,
    onFromDateChanged: (LocalDate) -> Unit,
    onToDateChanged: (LocalDate) -> Unit,
    totalAmount: Double?
) {
    val context = LocalContext.current
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd.MM.yyyy") }

    Column {
        InfoRow(label = "Начало", value = fromDate.format(dateFormatter), onClick = {
            showDatePicker(context, fromDate) { onFromDateChanged(it) }
        })
        HorizontalDivider(color = DividerColor, thickness = 1.dp)
        InfoRow(label = "Конец", value = toDate.format(dateFormatter), onClick = {
            showDatePicker(context, toDate) { onToDateChanged(it) }
        })
        HorizontalDivider(color = DividerColor, thickness = 1.dp)
        InfoRow(label = "Сумма", value = totalAmount?.toString()?.formatAsRuble() ?: "...")
    }
}

private fun showDatePicker(
    context: Context,
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
        },
        initialDate.year,
        initialDate.monthValue - 1,
        initialDate.dayOfMonth
    ).show()
}

@Composable
private fun HistoryEntryRow(item: Transaction, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(EditProfileBackgroundColor)
            .clickable(onClick = onClick)
            .height(70.dp)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(24.dp).clip(CircleShape).background(LightGreen),
            contentAlignment = Alignment.Center
        ) {
            Text(text = item.categoryEmoji, style = TextStyle(fontSize = 14.sp), maxLines = 1)
        }
        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(item.categoryName, style = MaterialTheme.typography.bodyLarge, color = Black)
            item.comment?.let {
                if (it.isNotBlank()) {
                    Text(it, style = MaterialTheme.typography.bodySmall, color = Black)
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(horizontalAlignment = Alignment.End) {
            Text(item.amount.toString().formatAsRuble(), style = MaterialTheme.typography.bodyLarge, color = Black)
            Spacer(modifier = Modifier.height(2.dp))
            Text(formatBackendTime(item.time), style = MaterialTheme.typography.bodySmall, color = Black)
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

private fun formatBackendTime(time: String): String {
    return try {
        val inputFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
        val outputFormatter = DateTimeFormatter.ofPattern("HH:mm")
        ZonedDateTime.parse(time, inputFormatter).format(outputFormatter)
    } catch (e: Exception) {
        "??:??"
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

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ExpensesHistoryScreenPreview() {
    FinGidTheme(darkTheme = false) {
        ExpensesHistoryScreen(navController = rememberNavController())
    }
}