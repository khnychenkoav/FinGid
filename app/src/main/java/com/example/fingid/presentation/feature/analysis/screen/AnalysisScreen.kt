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
import com.example.fingid.presentation.feature.main.model.ScreenConfig
import com.example.fingid.presentation.feature.main.model.TopBarConfig
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
    viewModel: AnalysisViewModel = daggerViewModel(),
    updateConfigState: (ScreenConfig) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var selectedTabIndex by remember { mutableStateOf(0) }

    LaunchedEffect(selectedTabIndex) {
        viewModel.onEvent(AnalysisEvent.OnLoadTransactions(isIncome = selectedTabIndex == 1))
    }

    LaunchedEffect(Unit) {
        updateConfigState(
            ScreenConfig(
                topBarConfig = TopBarConfig(titleResId = R.string.analysis_screen_title)
            )
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                text = { Text(stringResource(R.string.expense_screen_label)) }
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                text = { Text(stringResource(R.string.income_screen_label)) }
            )
        }

        when (val uiState = state) {
            is AnalysisState.Loading -> LoadingState()
            is AnalysisState.Error -> ErrorState(messageResId = uiState.messageResId, onRetry = {
                viewModel.onEvent(AnalysisEvent.OnLoadTransactions(isIncome = selectedTabIndex == 1))
            })
            is AnalysisState.Content -> {
                if (uiState.transactions.isEmpty()) {
                    EmptyState(messageResId = R.string.no_data_for_period)
                } else {
                    AnalysisView(state = uiState)
                }
            }
        }
    }
}

@Composable
fun AnalysisView(
    modifier: Modifier = Modifier,
    state: AnalysisState.Content
) {
    val scrollState = rememberScrollState()
    val totalSum = state.transactions.sumOf { it.amount }
    val currencySymbol = state.transactions.firstOrNull()?.account?.getCurrencySymbol() ?: ""

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState)
    ) {
        ListItemCard(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.onTertiaryContainer)
                .height(56.dp),
            item = ListItem(
                content = MainContent(title = stringResource(R.string.total_amount)),
                trail = TrailContent(text = "${totalSum.toString().formatWithSpaces()} $currencySymbol")
            )
        )

        state.transactions
            .groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
            .toList()
            .sortedByDescending { it.second }
            .forEach { (category, categorySum) ->
                val percentage = if (totalSum > 0) (categorySum.toDouble() / totalSum * 100).toInt() else 0
                ListItemCard(
                    item = ListItem(
                        lead = LeadContent.Text(text = category.emoji),
                        content = MainContent(title = category.name),
                        trail = TrailContent(
                            text = "${percentage}%",
                            subtext = "${categorySum.toString().formatWithSpaces()} $currencySymbol"
                        )
                    )
                )
            }

    }
}