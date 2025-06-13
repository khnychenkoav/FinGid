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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.fingid.domain.models.ExpenseEntryItem
import com.example.fingid.ui.theme.AppGreen
import com.example.fingid.ui.theme.Black
import com.example.fingid.ui.theme.DividerColor
import com.example.fingid.ui.theme.FinGidTheme
import com.example.fingid.ui.theme.LightGreen
import com.example.fingid.ui.theme.White
import com.example.fingid.utils.formatAsRuble

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisScreen(
    navController: NavController,
    startLabel: String = "февраль 2025",
    endLabel: String = "март 2025",
    entries: List<ExpenseEntryItem> = sampleAnalysis()
) {
    fun ExpenseEntryItem.numeric(): Long = amount.filter { it.isDigit() }.toLong()

    val total = remember(entries) { entries.sumOf { it.numeric() } }
    val pairs = remember(entries, total) {
        entries.mapIndexed { idx, e ->
            val pct = if (total == 0L) 0 else (e.numeric() * 100 / total).toInt()
            Triple(e, pct, sliceColor(idx))
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
            InfoRow(label = "Сумма", value = total.toString().formatAsRuble())
            HorizontalDivider(color = DividerColor, thickness = 1.dp)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(185.dp),
                contentAlignment = Alignment.Center
            ) {
                AnimatedDonutChart(pairs.map { it.second })
                LegendInsideDonut(pairs)
            }

            HorizontalDivider(color = DividerColor, thickness = 1.dp)

            LazyColumn {
                items(pairs, key = { it.first.id }) { (item, percent, color) ->
                    AnalysisEntryRow(item = item, percent = percent, onClick = { /* TODO */ })
                    HorizontalDivider(color = DividerColor, thickness = 1.dp)
                }
            }
        }
    }
}

@Composable
private fun AnimatedDonutChart(
    percentages: List<Int>,
    strokeWidthDp: Float = 24f
) {
    val animProgress = remember { Animatable(0f) }
    LaunchedEffect(percentages) {
        animProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
    }
    Canvas(modifier = Modifier.size(150.dp)) {
        val stroke = Stroke(width = strokeWidthDp)
        val radius = size.minDimension / 2
        val diameter = radius * 2
        var angleStart = -90f
        val clean = percentages.filter { it > 0 }
        clean.forEachIndexed { idx, pct ->
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
private fun AnalysisEntryRow(item: ExpenseEntryItem, percent: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(item.iconCircleBackgroundColor ?: LightGreen),
            contentAlignment = Alignment.Center
        ) {
            val txt = item.displayIcon ?: item.categoryName.split(' ').filter { it.isNotBlank() }.take(2)
                .joinToString("") { it.first().uppercase() }
            Text(txt, fontSize = 10.sp, color = Black)
        }
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f)) {
            Text(item.categoryName, style = MaterialTheme.typography.bodyLarge, color = Black)
            if (!item.subCategoryName.isNullOrBlank()) {
                Text(item.subCategoryName, style = MaterialTheme.typography.bodySmall, color = Black)
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text("$percent%", style = MaterialTheme.typography.bodyLarge, color = Black)
            Spacer(Modifier.height(2.dp))
            Text(item.amount, style = MaterialTheme.typography.bodyLarge, color = Black)
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
private fun LegendInsideDonut(data: List<Triple<ExpenseEntryItem, Int, Color>>) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        data.forEach { (item, pct, color) ->
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(0.dp)) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "$pct% ${item.categoryName}",
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

private fun sampleAnalysis(): List<ExpenseEntryItem> = listOf(
    ExpenseEntryItem(
        id = "remont",
        categoryName = "Ремонт квартиры",
        subCategoryName = "Ремонт – фурнитура для дверей",
        amount = "20000".formatAsRuble(),
        displayIcon = "PK",
        iconCircleBackgroundColor = LightGreen
    ),
    ExpenseEntryItem(
        id = "dog",
        categoryName = "На собачку",
        amount = "80000".formatAsRuble(),
        displayIcon = "🐶",
        iconCircleBackgroundColor = LightGreen
    )
)

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun AnalysisPreview() {
    FinGidTheme(darkTheme = false) {
        AnalysisScreen(navController = rememberNavController())
    }
}