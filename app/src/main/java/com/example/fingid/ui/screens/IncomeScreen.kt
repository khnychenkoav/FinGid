package com.example.fingid.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fingid.domain.models.IncomeEntryItem
import com.example.fingid.navigation.Screen
import com.example.fingid.ui.theme.DividerColor
import com.example.fingid.ui.theme.FinGidTheme
import com.example.fingid.ui.theme.LightGreen
import com.example.fingid.ui.theme.LightGrey
import com.example.fingid.ui.theme.White
import com.example.fingid.utils.formatAsRuble

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeScreen(navController: NavController) {
    val incomeEntries = remember {
        listOf(
            IncomeEntryItem(
                id = "total",
                categoryName = "Всего",
                amount = "600000".formatAsRuble(),
                isTotal = true,
                customBackgroundColor = LightGreen
            ),
            IncomeEntryItem(
                id = "salary",
                categoryName = "Зарплата",
                amount = "500000".formatAsRuble()
            ),
            IncomeEntryItem(
                id = "side_hustle",
                categoryName = "Подработка",
                amount = "100000".formatAsRuble()
            )
        )
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
                    IconButton(onClick = { /* TODO: Handle history click */ }) {
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
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Добавить доход",
                    tint = White
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            items(incomeEntries, key = { it.id }) { entry ->
                IncomeEntryRow(
                    item = entry,
                    onClick = {
                        if (!entry.isTotal) {
                            // TODO: Handle item click - navigate to edit income or details
                            println("Clicked on income item: ${entry.categoryName}")
                        }
                    }
                )
                HorizontalDivider(
                    color = DividerColor,
                    thickness = 1.dp
                )
            }
        }
    }
}

@Composable
fun IncomeEntryRow(item: IncomeEntryItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(item.customBackgroundColor ?: MaterialTheme.colorScheme.surface)
            .clickable(enabled = !item.isTotal) { onClick() }
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
                text = item.amount,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (item.showArrow) {
                Spacer(modifier = Modifier.size(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Подробнее",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun IncomeScreenPreview() {
    FinGidTheme(darkTheme = false) {
        IncomeScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun IncomeEntryRowPreview() {
    FinGidTheme {
        Column {
            IncomeEntryRow(
                IncomeEntryItem(
                    id = "total",
                    categoryName = "Всего",
                    amount = "600 000 ₽",
                    isTotal = true,
                    customBackgroundColor = LightGreen
                )
            ) {}
            HorizontalDivider(color = DividerColor)
            IncomeEntryRow(
                IncomeEntryItem(
                    id = "salary",
                    categoryName = "Зарплата",
                    amount = "500 000 ₽"
                )
            ) {}
            HorizontalDivider(color = DividerColor)
            IncomeEntryRow(
                IncomeEntryItem(
                    id = "other",
                    categoryName = "Другое без стрелки",
                    amount = "50 000 ₽",
                    showArrow = false
                )
            ) {}
        }
    }
}