package com.example.fingid.ui.screens

import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fingid.R
import com.example.fingid.domain.entities.Account // Убедитесь, что этот импорт есть
import com.example.fingid.domain.models.AccountInfoItem
import com.example.fingid.navigation.Screen
import com.example.fingid.ui.components.CurrencyBottomSheet
import com.example.fingid.ui.theme.*
import com.example.fingid.utils.formatAsRuble
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import kotlin.math.abs
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    navController: NavController,
    viewModel: AccountViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val iconCircleBalanceBg = Color(0xFFFFF3E0)
    val currencyRowBackgroundColor = LightGreen
    val chartData = remember { mockFeb2025() }
    var selectedCurrencySymbolForBottomSheet by remember { mutableStateOf("₽") }
    val currencySheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showCurrencyBottomSheet by remember { mutableStateOf(false) }

    when (val state = uiState) {
        is AccountUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is AccountUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Ошибка: ${state.message}")
            }
            LaunchedEffect(state.message) {
                Toast.makeText(context, "Ошибка загрузки: ${state.message}", Toast.LENGTH_LONG).show()
            }
        }
        is AccountUiState.Success -> {
            val account = state.account
            AccountScreenContent(
                navController = navController,
                account = account,
                chartData = chartData,
                selectedCurrencySymbolForBottomSheet = selectedCurrencySymbolForBottomSheet,
                onCurrencySymbolForBottomSheetChange = { selectedCurrencySymbolForBottomSheet = it },
                iconCircleBalanceBg = iconCircleBalanceBg,
                currencyRowBackgroundColor = currencyRowBackgroundColor,
                showCurrencyBottomSheet = showCurrencyBottomSheet,
                onShowCurrencyBottomSheetChange = { showCurrencyBottomSheet = it },
                currencySheetState = currencySheetState
            )
        }
        AccountUiState.Idle -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Загрузка данных счета...")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreenContent(
    navController: NavController,
    account: Account,
    chartData: List<BarEntry>,
    selectedCurrencySymbolForBottomSheet: String,
    onCurrencySymbolForBottomSheetChange: (String) -> Unit,
    iconCircleBalanceBg: Color,
    currencyRowBackgroundColor: Color,
    showCurrencyBottomSheet: Boolean,
    onShowCurrencyBottomSheetChange: (Boolean) -> Unit,
    currencySheetState: SheetState
) {
    val accountInfoItems = remember(account, selectedCurrencySymbolForBottomSheet, currencyRowBackgroundColor) {
        listOf(
            AccountInfoItem(
                id = "balance",
                title = "Баланс (${account.name})",
                displayIcon = "💰",
                displayIconColor = Black,
                iconCircleBackgroundColor = iconCircleBalanceBg,
                value = account.balance.toPlainString().formatAsRuble(account.currency),
                valueColor = Black,
                backgroundColor = LightGreen,
                showArrow = true
            ),
            AccountInfoItem(
                id = "currency",
                title = "Валюта счета",
                displayIcon = null,
                iconCircleBackgroundColor = null,
                value = account.currency,
                valueColor = Black,
                backgroundColor = currencyRowBackgroundColor,
                showArrow = true
            )
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(account.name, style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate(
                            Screen.EditAccount.createRoute(
                                accountId = account.id,
                                balanceValue = account.balance.toPlainString()
                            )
                        )
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_pencil),
                            contentDescription = "Редактировать счет",
                            tint = LightGrey
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Handle FAB click - Add Transaction for this account */ },
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
                    contentDescription = "Добавить операцию",
                    tint = White
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            accountInfoItems.forEach { item ->
                AccountInfoRow(item = item, onClick = {
                    if (item.id == "balance") {
                        navController.navigate(
                            Screen.EditAccount.createRoute(
                                accountId = account.id,
                                balanceValue = account.balance.toPlainString()
                            )
                        )
                    } else if (item.id == "currency") {
                        onShowCurrencyBottomSheetChange(true)
                    }
                })
                if (item.id == "balance" && accountInfoItems.indexOf(item) < accountInfoItems.size -1) {
                    HorizontalDivider(
                        color = DividerColor,
                        thickness = 0.5.dp
                    )
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            BarChart(
                raw = chartData,
                month = YearMonth.of(2025, 2),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 16.dp)
            )
        }

        if (showCurrencyBottomSheet) {
            CurrencyBottomSheet(
                sheetState = currencySheetState,
                onDismiss = { onShowCurrencyBottomSheetChange(false) },
                onCurrencySelected = { currency ->
                    // TODO: Implement actual currency update via ViewModel
                    onCurrencySymbolForBottomSheetChange(currency.symbol)
                    println("Выбрана валюта для отображения (не сохранено): ${currency.name}")
                    onShowCurrencyBottomSheetChange(false)
                }
            )
        }
    }
}

@Composable
fun AccountInfoRow(item: AccountInfoItem, onClick: () -> Unit = {}) {
    val iconCircleSize = 32.dp
    val emojiBaseFontSize = 16.sp
    val symbolBaseFontSize = 18.sp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp)
            .background(item.backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (item.displayIcon != null && item.iconCircleBackgroundColor != null) {
            Box(
                modifier = Modifier
                    .size(iconCircleSize)
                    .clip(CircleShape)
                    .background(item.iconCircleBackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.displayIcon,
                    style = TextStyle(
                        fontSize = if (isEmoji(item.displayIcon)) emojiBaseFontSize else symbolBaseFontSize,
                        fontWeight = FontWeight.Normal,
                        color = item.displayIconColor ?: Black,
                        textAlign = TextAlign.Center
                    ),
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
        }

        Text(
            text = item.title,
            style = MaterialTheme.typography.bodyLarge,
            color = Black
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = item.value,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            color = item.valueColor ?: Black
        )
        if (item.showArrow) {
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_more_vert),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

data class BarEntry(val date: LocalDate, val value: Float)

@Composable
fun BarChart(
    raw: List<BarEntry>,
    month: YearMonth = YearMonth.now(),
    modifier: Modifier = Modifier,
    positiveColor: Color = BrightOrange,
    negativeColor: Color = AppGreen,
    maxBarWidthDp: Dp = 8.dp,
    duration: Int = 800
) {
    val entries = remember(raw, month) {
        val map = raw.associateBy { it.date }
        (1..month.lengthOfMonth()).map { d ->
            val date = month.atDay(d)
            map[date] ?: BarEntry(date, 0f)
        }
    }
    if (entries.isEmpty()) return

    val density = LocalDensity.current
    val maxBarWidthPx = with(density) { maxBarWidthDp.toPx() }
    val maxAbs = entries.maxOfOrNull { abs(it.value) }?.coerceAtLeast(1f) ?: 1f
    val midDate = month.atDay((entries.size + 1) / 2)
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd.MM") }

    val progress = remember { Animatable(0f) }
    LaunchedEffect(entries) {
        progress.animateTo(
            1f,
            animationSpec = tween(durationMillis = duration, easing = FastOutSlowInEasing)
        )
    }

    Column(modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            val slot = size.width / entries.size
            val barWidth = min(slot * .40f, maxBarWidthPx)
            val radius = barWidth / 2

            entries.forEachIndexed { index, entry ->
                val barHeight = (abs(entry.value) / maxAbs) * size.height * progress.value
                if (barHeight == 0f) return@forEachIndexed
                val left = index * slot + (slot - barWidth) / 2
                val top = size.height - barHeight
                val color = if (entry.value >= 0) positiveColor else negativeColor

                drawRoundRect(
                    color = color,
                    topLeft = Offset(left, top),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(radius)
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf(
                month.atDay(1),
                midDate,
                month.atEndOfMonth()
            ).forEach {
                Text(
                    it.format(dateFormatter),
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                )
            }
        }
    }
}


fun mockFeb2025(): List<BarEntry> = listOf(
    10, 15, 45, 40, 80, -55, 55,
    11, 210, -132, 11, 250, 42, 190,
    89, -20, -15, -66, 60, 65, 22,
    70, -22, -15, 16, 80, 19, 95
).mapIndexed { i, v ->
    BarEntry(LocalDate.of(2025, 2, i + 1), v.toFloat())
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AccountScreenPreview() {
    FinGidTheme(darkTheme = false) {
        val navController = rememberNavController()
        val mockAccount = Account(
            id = 1L,
            name = "Тестовый Счет Preview",
            balance = BigDecimal("-670000.50"),
            currency = "RUB",
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now()
        )
        AccountScreenContent(
            navController = navController,
            account = mockAccount,
            chartData = mockFeb2025(),
            selectedCurrencySymbolForBottomSheet = "₽",
            onCurrencySymbolForBottomSheetChange = {},
            iconCircleBalanceBg = Color(0xFFFFF3E0),
            currencyRowBackgroundColor = LightGreen,
            showCurrencyBottomSheet = false,
            onShowCurrencyBottomSheetChange = {},
            currencySheetState = rememberModalBottomSheetState()
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AccountInfoRowPreview() {
    FinGidTheme {
        val iconCircleBalanceBg = Color(0xFFFFF3E0)
        val currencyRowBg = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        Column {
            AccountInfoRow(
                AccountInfoItem(
                    id = "1",
                    title = "Баланс",
                    displayIcon = "💰",
                    displayIconColor = Black,
                    iconCircleBackgroundColor = iconCircleBalanceBg,
                    value = "-100000",
                    valueColor = Black,
                    showArrow = true,
                    backgroundColor = LightGreen
                )
            )
            HorizontalDivider()
            AccountInfoRow(
                AccountInfoItem(
                    id = "2",
                    title = "Валюта",
                    displayIcon = null,
                    displayIconColor = null,
                    iconCircleBackgroundColor = null,
                    value = "₽",
                    valueColor = Black,
                    showArrow = true,
                    backgroundColor = currencyRowBg
                )
            )
            HorizontalDivider()
            AccountInfoRow(
                AccountInfoItem(
                    id = "3",
                    title = "Без иконки и стрелки",
                    displayIcon = null,
                    displayIconColor = null,
                    iconCircleBackgroundColor = null,
                    value = "Значение",
                    valueColor = Black,
                    showArrow = false,
                    backgroundColor = Color.LightGray.copy(alpha = 0.2f)
                )
            )
        }
    }
}