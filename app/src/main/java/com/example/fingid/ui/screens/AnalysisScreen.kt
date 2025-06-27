package com.example.fingid.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fingid.R
import com.example.fingid.domain.model.Transaction
import com.example.fingid.ui.theme.*
import com.example.fingid.utils.formatAsRuble
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisScreen(
    navController: NavController,
    startLabel: String,
    endLabel: String,

    transactions: List<Transaction> = sampleAnalysisTransactions()
) {
    val aggregatedData = remember(transactions) {
        transactions
            .groupBy { it.categoryName }
            .map { (categoryName, transactionList) ->
                val totalAmount = transactionList.sumOf { it.amount }
                val emoji = transactionList.first().categoryEmoji
                AggregatedTransaction(
                    categoryName = categoryName,
                    totalAmount = totalAmount,
                    emoji = emoji
                )
            }
    }

    val totalAmount = remember(aggregatedData) { aggregatedData.sumOf { it.totalAmount } }

    val chartSlices = remember(aggregatedData, totalAmount) {
        aggregatedData.mapIndexed { index, data ->
            val percentage = if (totalAmount > 0) (data.totalAmount / totalAmount * 100).toFloat() else 0f
            ChartSlice(data, percentage, sliceColor(index))
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = Black)
                    }
                },
                title = { Text("Анализ", style = MaterialTheme.typography.titleLarge, color = Black) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = White,
                    navigationIconContentColor = Black,
                    titleContentColor = Black
                )
            )
        }
    ) { pv ->
        Column(
            modifier = Modifier
                .padding(pv)
                .fillMaxSize()
        ) {
            PeriodRow(label = "Период: начало", value = startLabel)
            HorizontalDivider(color = DividerColor, thickness = 1.dp)
            PeriodRow(label = "Период: конец", value = endLabel)
            HorizontalDivider(color = DividerColor, thickness = 1.dp)
            InfoRow(label = "Сумма", value = totalAmount.toString().formatAsRuble())
            HorizontalDivider(color = DividerColor, thickness = 1.dp)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(185.dp),
                contentAlignment = Alignment.Center
            ) {
                AnimatedDonutChart(chartSlices.map { it.percentage })
                LegendInsideDonut(chartSlices)
            }

            HorizontalDivider(color = DividerColor, thickness = 1.dp)

            LazyColumn {
                items(chartSlices, key = { it.data.categoryName }) { slice ->
                    AnalysisEntryRow(
                        item = slice.data,
                        percent = slice.percentage.roundToInt(),
                        onClick = { /* TODO */ }
                    )
                    HorizontalDivider(color = DividerColor, thickness = 1.dp)
                }
            }
        }
    }
}

data class AggregatedTransaction(
    val categoryName: String,
    val totalAmount: Double,
    val emoji: String
)

data class ChartSlice(
    val data: AggregatedTransaction,
    val percentage: Float,
    val color: Color
)

@Composable
private fun AnimatedDonutChart(
    percentages: List<Float>,
    strokeWidthDp: Float = 24f
) {
    val animProgress = remember { Animatable(0f) }
    LaunchedEffect(percentages) {
        animProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
        )
    }
    Canvas(modifier = Modifier.size(150.dp)) {
        val stroke = Stroke(width = strokeWidthDp)
        val radius = size.minDimension / 2
        val diameter = radius * 2
        var angleStart = -90f
        val cleanPercentages = percentages.filter { it > 0 }
        cleanPercentages.forEachIndexed { idx, pct ->
            val fullSweep = 360 * (pct / 100f)
            val sweep = fullSweep * animProgress.value
            drawArc(
                color = sliceColor(idx),
                startAngle = angleStart,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = Offset((size.width - diameter) / 2, (size.height - diameter) / 2),
                size = Size(diameter, diameter),
                style = stroke
            )
            angleStart += fullSweep
        }
    }
}

@Composable
private fun PeriodRow(label: String, value: String) {
    InfoRow(label = label) {
        FilledTonalButton(
            onClick = { /* TODO date picker */ },
            shape = RoundedCornerShape(percent = 50),
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .defaultMinSize(minWidth = 100.dp),
            colors = androidx.compose.material3.ButtonDefaults.filledTonalButtonColors(
                containerColor = AppGreen,
                contentColor = Black
            ),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp)
        ) {
            Text(value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String? = null,
    content: @Composable RowScope.() -> Unit = {
        value?.let { Text(it, style = MaterialTheme.typography.bodyLarge, color = Black) }
    }
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        content = {
            Text(label, style = MaterialTheme.typography.bodyLarge, color = Black)
            content()
        }
    )
}

@Composable
private fun AnalysisEntryRow(item: AggregatedTransaction, percent: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().height(72.dp).clickable { onClick() }.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(24.dp).clip(CircleShape).background(LightGreen),
            contentAlignment = Alignment.Center
        ) {
            Text(item.emoji, fontSize = 10.sp, color = Black)
        }
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f)) {
            Text(item.categoryName, style = MaterialTheme.typography.bodyLarge, color = Black)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text("$percent%", style = MaterialTheme.typography.bodyLarge, color = Black)
            Spacer(Modifier.height(2.dp))
            Text(item.totalAmount.toString().formatAsRuble(), style = MaterialTheme.typography.bodyLarge, color = Black)
        }
        Spacer(Modifier.width(8.dp))
        Icon(
            painter = androidx.compose.ui.res.painterResource(id = R.drawable.ic_more_vert),
            contentDescription = "Подробнее",
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun LegendInsideDonut(data: List<ChartSlice>) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        data.forEach { (aggregatedData, percentage, color) ->
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(0.dp)) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "${percentage.roundToInt()}% ${aggregatedData.categoryName}",
                    fontSize = 8.sp,
                    lineHeight = 8.sp,
                    color = Black
                )
            }
            Spacer(Modifier.height(4.dp))
        }
    }
}

private fun sliceColor(index: Int): Color = when (index % 4) {
    0 -> AppGreen
    1 -> Color(0xFFFFD600)
    2 -> Color(0xFFFF6D00)
    else -> Color(0xFF2962FF)
}

private fun sampleAnalysisTransactions(): List<Transaction> = listOf(
    Transaction(1, "1", "Ремонт", "🔨", false, 20000.0, "", null),
    Transaction(2, "1", "На собачку", "🐶", false, 80000.0, "", null),
    Transaction(3, "1", "Ремонт", "🔨", false, 5000.0, "", "фурнитура")
)

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun AnalysisPreview() {
    FinGidTheme(darkTheme = false) {
        AnalysisScreen(navController = rememberNavController(), startLabel = "Начало", endLabel = "Конец")
    }
}