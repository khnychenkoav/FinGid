package com.example.fingid.presentation.feature.analysis.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fingid.R
import com.example.fingid.core.utils.Constants
import com.example.fingid.data.remote.api.AppError
import com.example.fingid.domain.model.TransactionResponseDomain
import com.example.fingid.domain.usecases.GetExpensesByPeriodUseCase
import com.example.fingid.domain.usecases.GetIncomesByPeriodUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AnalysisState {
    data object Loading : AnalysisState
    data class Error(@StringRes val messageResId: Int) : AnalysisState
    data class Content(val transactions: List<TransactionResponseDomain>) : AnalysisState
}

sealed class AnalysisEvent {
    data class OnLoadTransactions(val isIncome: Boolean) : AnalysisEvent()
}

class AnalysisViewModel @Inject constructor(
    private val getExpensesUseCase: GetExpensesByPeriodUseCase,
    private val getIncomesUseCase: GetIncomesByPeriodUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<AnalysisState>(AnalysisState.Loading)
    val state: StateFlow<AnalysisState> = _state.asStateFlow()

    fun onEvent(event: AnalysisEvent) {
        when (event) {
            is AnalysisEvent.OnLoadTransactions -> loadTransactions(event.isIncome)
        }
    }

    private fun loadTransactions(isIncome: Boolean) {
        viewModelScope.launch {
            _state.value = AnalysisState.Loading

            val result = if (isIncome) {
                getIncomesUseCase(Constants.TEST_ACCOUNT_ID, null, null)
            } else {
                getExpensesUseCase(Constants.TEST_ACCOUNT_ID, null, null)
            }

            result.onSuccess { transactions ->
                _state.value = AnalysisState.Content(transactions)
            }.onFailure { error ->
                val messageResId = (error as? AppError)?.messageResId ?: R.string.unknown_error
                _state.value = AnalysisState.Error(messageResId)
            }
        }
    }
}