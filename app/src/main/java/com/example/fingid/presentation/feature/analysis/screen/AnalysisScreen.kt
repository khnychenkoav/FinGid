package com.example.fingid.presentation.feature.analysis.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fingid.R
import com.example.fingid.core.di.daggerViewModel
import com.example.fingid.core.utils.formatWithSpaces
import com.example.fingid.presentation.feature.analysis.viewmodel.AnalysisEvent
import com.example.fingid.presentation.feature.analysis.viewmodel.AnalysisState
import com.example.fingid.presentation.feature.analysis.viewmodel.AnalysisViewModel
import com.example.fingid.presentation.feature.history.component.DateSelectionHeader
import com.example.fingid.presentation.feature.history.viewmodel.DateType
import com.example.fingid.presentation.feature.history.viewmodel.HistoryScreenState
import com.example.fingid.presentation.feature.history.viewmodel.HistoryScreenViewModel
import com.example.fingid.presentation.feature.main.model.ScreenConfig
import com.example.fingid.presentation.feature.main.model.TopBarBackAction
import com.example.fingid.presentation.feature.main.model.TopBarConfig
import com.example.fingid.presentation.shared.components.DatePickerModal
import com.example.fingid.presentation.shared.components.EmptyState
import com.example.fingid.presentation.shared.components.ErrorState
import com.example.fingid.presentation.shared.components.ListItemCard
import com.example.fingid.presentation.shared.components.LoadingState
import com.example.fingid.presentation.shared.model.LeadContent
import com.example.fingid.presentation.shared.model.ListItem
import com.example.fingid.presentation.shared.model.MainContent
import com.example.fingid.presentation.shared.model.TrailContent

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AnalysisScreen(
    viewModel: HistoryScreenViewModel = daggerViewModel(),
    updateConfigState: (ScreenConfig) -> Unit,
    isIncome: Boolean,
    onBackNavigate: () -> Unit
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()
    val startDate by viewModel.historyStartDate.collectAsStateWithLifecycle()
    val endDate by viewModel.historyEndDate.collectAsStateWithLifecycle()
    val showDatePickerModal by viewModel.showDatePickerModal.collectAsStateWithLifecycle()

    LaunchedEffect(isIncome) {
        viewModel.setHistoryTransactionsType(isIncome)
        viewModel.initialize()
    }

    LaunchedEffect(Unit) {
        updateConfigState(
            ScreenConfig(
                topBarConfig = TopBarConfig(
                    titleResId = R.string.analysis_screen_title,
                    backAction = TopBarBackAction(actionUnit = onBackNavigate)
                )
            )
        )
    }

    Column(Modifier.fillMaxSize()) {
        DateSelectionHeader(
            startDate = startDate,
            onStartDate = { viewModel.showDatePickerModal(DateType.START) },
            endDate = endDate,
            onEndDate = { viewModel.showDatePickerModal(DateType.END) }
        )

        when (val uiState = state) {
            is HistoryScreenState.Loading -> LoadingState()
            is HistoryScreenState.Error -> ErrorState(
                messageResId = uiState.messageResId,
                onRetry = uiState.retryAction
            )
            is HistoryScreenState.Empty -> EmptyState(messageResId = R.string.no_data_for_period)
            is HistoryScreenState.Success -> AnalysisView(state = uiState)
        }
    }

    if (showDatePickerModal) {
        DatePickerModal(
            onDateSelected = { viewModel.confirmDateSelection(it) },
            onDismiss = { viewModel.onDismissDatePicker() }
        )
    }
}


@Composable
fun AnalysisView(
    modifier: Modifier = Modifier,
    state: HistoryScreenState.Success,
) {
    val groupedData = state.transactions
        .groupBy { it.title }
        .mapValues { entry -> entry.value.sumOf { it.amount }.toFloat() }

    val chartDataForModule = com.example.feature_chart.ChartData(
        points = groupedData.map { (label, value) ->
            com.example.feature_chart.ChartDataPoint(label = label, value = value)
        }.sortedByDescending { it.value }
    )


    val scrollState = rememberScrollState()

    val groupedTransactions = state.transactions.groupBy { it.title }
    val totalSum = state.transactions.sumOf { it.amount }
    val currencySymbol = state.transactions.firstOrNull()?.currency ?: ""

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState)
    ) {

        if (chartDataForModule.points.isNotEmpty()) {
            com.example.feature_chart.SimpleBarChart(chartData = chartDataForModule)
        }

        ListItemCard(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.onTertiaryContainer)
                .height(56.dp),
            item = ListItem(
                content = MainContent(title = stringResource(R.string.total_amount)),
                trail = TrailContent(text = "${totalSum.toString().formatWithSpaces()} $currencySymbol")
            )
        )

        groupedTransactions
            .mapValues { entry -> entry.value.sumOf { it.amount } }
            .toList()
            .sortedByDescending { it.second }
            .forEach { (categoryName, categorySum) ->
                val emoji = state.transactions.first { it.title == categoryName }.emoji
                val percentage = if (totalSum > 0) (categorySum.toDouble() / totalSum * 100).toInt() else 0

                ListItemCard(
                    modifier = Modifier.height(70.dp),
                    item = ListItem(
                        lead = LeadContent.Text(text = emoji),
                        content = MainContent(title = categoryName),
                        trail = TrailContent(
                            text = "${percentage}%",
                            subtext = "${categorySum.toString().formatWithSpaces()} $currencySymbol"
                        )
                    )
                )
            }
    }
}