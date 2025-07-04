package com.example.fingid.presentation.feature.main.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fingid.R
import com.example.fingid.core.navigation.Route
import com.example.fingid.presentation.feature.main.model.FloatingActionConfig
import com.example.fingid.presentation.feature.main.model.ScreenConfig
import com.example.fingid.presentation.feature.main.model.TopBarAction
import com.example.fingid.presentation.feature.main.model.TopBarConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class MainScreenViewModel @Inject constructor() : ViewModel() {

    private val _configState = MutableStateFlow(
        ScreenConfig(
            route = Route.Root.Expenses.path,
            topBarConfig = TopBarConfig(
                titleResId = R.string.expense_screen_title,
                action = TopBarAction(
                    iconResId = R.drawable.ic_history,
                    descriptionResId = R.string.expenses_history_description,
                    actionRoute = Route.SubScreens.History.route(income = false)
                )
            ),
            floatingActionConfig = FloatingActionConfig(
                descriptionResId = R.string.add_expense_description,
                actionRoute = Route.Root.Expenses.path
            )
        )
    )

    val configState: StateFlow<ScreenConfig> = _configState


    fun updateConfigForScreen(config: ScreenConfig) {
        _configState.value = config
    }
}