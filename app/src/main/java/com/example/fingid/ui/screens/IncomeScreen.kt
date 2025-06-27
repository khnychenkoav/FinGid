package com.example.fingid.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fingid.R
import com.example.fingid.domain.model.Transaction
import com.example.fingid.navigation.Screen
import com.example.fingid.ui.commonitems.UiState
import com.example.fingid.ui.theme.DividerColor
import com.example.fingid.ui.theme.FinGidTheme
import com.example.fingid.ui.theme.LightGreen
import com.example.fingid.ui.theme.LightGrey
import com.example.fingid.ui.theme.White
import com.example.fingid.utils.formatAsRuble
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: IncomeScreenViewModel = viewModel(factory = IncomeScreenViewModelFactory())
    val incomeState by viewModel.incomeList.observeAsState(initial = UiState.Loading)

    LaunchedEffect(Unit) {
        val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        viewModel.loadIncomes(today, today, context)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Доходы сегодня", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = { /* TODO: Navigate to Income History */ }) {
                        Icon(
                            imageVector = Icons.Outlined.History,
                            contentDescription = "История доходов",
                            tint = LightGrey
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddEditIncome.createRoute(null))
                },
                modifier = Modifier.offset(y = 26.dp),
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp, pressedElevation = 0.dp)
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Добавить доход", tint = White)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when (val state = incomeState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is UiState.Error -> {
                    Text(
                        text = state.message ?: "Неизвестная ошибка",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is UiState.Success -> {
                    val transactions = state.data
                    val totalAmount = transactions.sumOf { it.amount }
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        item {
                            IncomeTotalRow(
                                title = "Всего",
                                amount = totalAmount,
                                backgroundColor = LightGreen
                            )
                            HorizontalDivider(color = DividerColor, thickness = 1.dp)
                        }
                        items(transactions, key = { it.id }) { transaction ->
                            IncomeEntryRow(
                                item = transaction,
                                onClick = { /* TODO: Handle item click */ }
                            )
                            HorizontalDivider(color = DividerColor, thickness = 1.dp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IncomeTotalRow(title: String, amount: Double, backgroundColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = amount.toString().formatAsRuble(),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun IncomeEntryRow(item: Transaction, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = item.categoryName,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = item.amount.toString().formatAsRuble(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.size(8.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_more_vert),
                contentDescription = "Подробнее",
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun IncomeScreenPreview() {
    FinGidTheme(darkTheme = false) {
        IncomeScreen(navController = rememberNavController())
    }
}