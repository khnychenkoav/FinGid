package com.example.feature_chart


data class ChartDataPoint(
    val label: String,
    val value: Float
)

data class ChartData(
    val points: List<ChartDataPoint>
)