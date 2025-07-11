package com.example.fingid.presentation.feature.balance.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fingid.R
import com.example.fingid.core.di.daggerViewModel
import com.example.fingid.presentation.feature.balance.model.BalanceUiModel
import com.example.fingid.presentation.feature.balance.viewmodel.BalanceScreenViewModel
import com.example.fingid.presentation.feature.balance.viewmodel.BalanceUiState
import com.example.fingid.presentation.feature.main.model.FloatingActionConfig
import com.example.fingid.presentation.feature.main.model.ScreenConfig
import com.example.fingid.presentation.feature.main.model.TopBarAction
import com.example.fingid.presentation.feature.main.model.TopBarConfig
import com.example.fingid.presentation.shared.components.ErrorState
import com.example.fingid.presentation.shared.components.ListItemCard
import com.example.fingid.presentation.shared.components.LoadingState
import com.example.fingid.presentation.shared.model.LeadContent
import com.example.fingid.presentation.shared.model.ListItem
import com.example.fingid.presentation.shared.model.MainContent
import com.example.fingid.presentation.shared.model.TrailContent

@Composable
fun BalanceScreen(
    viewModel: BalanceScreenViewModel = daggerViewModel(),
    updateConfigState: (ScreenConfig) -> Unit,
    onEditNavigate: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.init() }

    LaunchedEffect(Unit) {
        updateConfigState(
            ScreenConfig(
                topBarConfig = TopBarConfig(
                    titleResId = R.string.balance_screen_title,
                    action = TopBarAction(
                        iconResId = R.drawable.ic_edit,
                        descriptionResId = R.string.balance_edit_description,
                        actionUnit = { onEditNavigate(viewModel.getAccountId()) }
                    )
                ),
                floatingActionConfig = FloatingActionConfig(
                    descriptionResId = R.string.add_balance_description,
                    actionUnit = {}
                )
            )
        )
    }

    when (uiState) {
        BalanceUiState.Loading -> LoadingState()
        is BalanceUiState.Error -> ErrorState(
            messageResId = (uiState as BalanceUiState.Error).messageResId,
            onRetry = viewModel::init
        )

        is BalanceUiState.Content -> BalanceContentState(
            balance = (uiState as BalanceUiState.Content).balance
        )
    }
}

@Composable
private fun BalanceContentState(
    balance: BalanceUiModel
) {
    Column(Modifier.fillMaxSize()) {
        ListItemCard(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.onTertiaryContainer)
                .height(56.dp),
            item = ListItem(
                lead = LeadContent.Text(text = "💰", color = MaterialTheme.colorScheme.background),
                content = MainContent(title = stringResource(R.string.balance)),
                trail = TrailContent(text = balance.balanceFormatted)
            ),
        )
        ListItemCard(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.onTertiaryContainer)
                .height(56.dp),
            item = ListItem(
                content = MainContent(title = stringResource(R.string.currency)),
                trail = TrailContent(text = balance.currency)
            ),
            showDivider = false
        )
    }
}