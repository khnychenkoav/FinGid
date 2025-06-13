package com.example.fingid.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.fingid.ui.theme.Black
import com.example.fingid.ui.theme.DividerColor
import com.example.fingid.ui.theme.FinGidTheme
import com.example.fingid.ui.theme.LightGrey
import com.example.fingid.ui.theme.White
import com.example.fingid.utils.formatAsRuble
import com.example.fingid.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen(navController: NavController) {
    val iconBgColor = MaterialTheme.colorScheme.primaryContainer

    val expenseEntries = remember {
        listOf(
            ExpenseEntryItem(
                id = "total",
                categoryName = "Всего",
                amount = "436558".formatAsRuble(),
                isTotal = true,
                customBackgroundColor = iconBgColor
            ),
            ExpenseEntryItem(
                id = "rent",
                categoryName = "Аренда квартиры",
                amount = "100000".formatAsRuble(),
                displayIcon = "🏠",
                iconCircleBackgroundColor = iconBgColor
            ),
            ExpenseEntryItem(
                id = "clothes",
                categoryName = "Одежда",
                amount = "100000".formatAsRuble(),
                displayIcon = "👗",
                iconCircleBackgroundColor = iconBgColor
            ),
            ExpenseEntryItem(
                id = "dog_jack",
                categoryName = "На собачку",
                subCategoryName = "Джек",
                amount = "100000".formatAsRuble(),
                displayIcon = "🐶",
                iconCircleBackgroundColor = iconBgColor
            ),
            ExpenseEntryItem(
                id = "dog_annie",
                categoryName = "На собачку",
                subCategoryName = "Энни",
                amount = "100000".formatAsRuble(),
                displayIcon = "🐶",
                iconCircleBackgroundColor = iconBgColor
            ),
            ExpenseEntryItem(
                id = "renovation",
                categoryName = "Ремонт квартиры",
                amount = "100000".formatAsRuble(),
                displayIcon = "РК",
                iconCircleBackgroundColor = iconBgColor
            ),
            ExpenseEntryItem(
                id = "products",
                categoryName = "Продукты",
                amount = "100000".formatAsRuble(),
                displayIcon = "🍭",
                iconCircleBackgroundColor = iconBgColor
            ),
            ExpenseEntryItem(
                id = "gym",
                categoryName = "Спортзал",
                amount = "100000".formatAsRuble(),
                displayIcon = "🏋️",
                iconCircleBackgroundColor = iconBgColor
            ),
            ExpenseEntryItem(
                id = "medicine",
                categoryName = "Медицина",
                amount = "100000".formatAsRuble(),
                displayIcon = "💊",
                iconCircleBackgroundColor = iconBgColor
            )
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Расходы сегодня", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.ExpensesHistory.route)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.History,
                            contentDescription = "История расходов",
                            tint = LightGrey
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddEditExpense.createRoute(null))
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
                    contentDescription = "Добавить расход",
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
            items(expenseEntries, key = { it.id }) { entry ->
                ExpenseEntryRow(
                    item = entry,
                    onClick = {
                        if (!entry.isTotal) {
                            // TODO: Handle item click - navigate to edit expense or details
                            println("Clicked on expense item: ${entry.categoryName}")
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
fun ExpenseEntryRow(item: ExpenseEntryItem, onClick: () -> Unit) {
    val rowHeight = if (!item.isTotal) 70.dp else 56.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(item.customBackgroundColor ?: MaterialTheme.colorScheme.surface)
            .clickable(enabled = !item.isTotal) { onClick() }
            .height(IntrinsicSize.Min)
            .defaultMinSize(minHeight = rowHeight)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (item.isTotal) {
            Text(
                text = item.categoryName,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(item.iconCircleBackgroundColor ?: Color.Transparent),
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
                    style = TextStyle(
                        fontSize = if (isEmoji(displayText)) 16.sp else 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Black,
                        textAlign = TextAlign.Center
                    ),
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.categoryName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (item.subCategoryName != null) {
                    Text(
                        text = item.subCategoryName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = item.amount,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (item.showArrow) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_more_vert),
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
fun ExpensesScreenPreview() {
    FinGidTheme(darkTheme = false) {
        ExpensesScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseEntryRowPreview() {
    val iconBg = MaterialTheme.colorScheme.primaryContainer
    FinGidTheme {
        Column {
            ExpenseEntryRow(
                ExpenseEntryItem(
                    id = "total",
                    categoryName = "Всего",
                    amount = "436 558 ₽",
                    isTotal = true,
                    customBackgroundColor = iconBg
                )
            ) {}
            HorizontalDivider(color = DividerColor)
            ExpenseEntryRow(
                ExpenseEntryItem(
                    id = "rent",
                    categoryName = "Аренда квартиры",
                    amount = "100 000 ₽",
                    displayIcon = "🏠",
                    iconCircleBackgroundColor = iconBg
                )
            ) {}
            HorizontalDivider(color = DividerColor)
            ExpenseEntryRow(
                ExpenseEntryItem(
                    id = "dog_jack",
                    categoryName = "На собачку",
                    subCategoryName = "Джек",
                    amount = "100 000 ₽",
                    displayIcon = "🐶",
                    iconCircleBackgroundColor = iconBg
                )
            ) {}
            HorizontalDivider(color = DividerColor)
            ExpenseEntryRow(
                ExpenseEntryItem(
                    id = "renovation",
                    categoryName = "Ремонт квартиры",
                    amount = "100 000 ₽",
                    displayIcon = "РК",
                    iconCircleBackgroundColor = iconBg
                )
            ) {}
        }
    }
}