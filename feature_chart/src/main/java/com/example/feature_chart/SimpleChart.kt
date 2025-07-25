package com.example.feature_chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp


@Composable
fun SimpleBarChart(
    chartData: ChartData,
    modifier: Modifier = Modifier
) {
    val maxValue = chartData.points.maxOfOrNull { it.value } ?: 1f

    Column(modifier = modifier.padding(16.dp)) {
        Text("Анализ по категориям:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        chartData.points.forEach { point ->
            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = point.label, modifier = Modifier.width(80.dp), maxLines = 1)
                val barWidthFraction = if (maxValue > 0) point.value / maxValue else 0f
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = barWidthFraction)
                        .height(24.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
        }
    }
}