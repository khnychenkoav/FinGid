package com.example.fingid.presentation.feature.expenses.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fingid.R
import com.example.fingid.core.utils.Constants
import com.example.fingid.core.utils.getCurrentDate
import com.example.fingid.data.remote.api.AppError
import com.example.fingid.domain.usecases.GetExpensesByPeriodUseCase
import com.example.fingid.presentation.feature.expenses.mapper.TransactionToExpenseMapper
import com.example.fingid.presentation.feature.expenses.model.ExpenseUiModel
import com.example.fingid.presentation.feature.expenses.viewmodel.ExpensesUiState.Content
import com.example.fingid.presentation.feature.expenses.viewmodel.ExpensesUiState.Empty
import com.example.fingid.presentation.feature.expenses.viewmodel.ExpensesUiState.Error
import com.example.fingid.presentation.feature.expenses.viewmodel.ExpensesUiState.Loading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface ExpensesUiState {

    data object Loading : ExpensesUiState


    data class Content(
        val expenses: List<ExpenseUiModel>,
        val totalAmount: String
    ) : ExpensesUiState

    data object Empty : ExpensesUiState

    data class Error(@StringRes val messageResId: Int) : ExpensesUiState
}


class ExpensesScreenViewModel @Inject constructor(
    private val getTransactionsByPeriod: GetExpensesByPeriodUseCase,
    private val mapper: TransactionToExpenseMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow<ExpensesUiState>(Loading)
    val uiState: StateFlow<ExpensesUiState> = _uiState.asStateFlow()


    fun init() = viewModelScope.launch(Dispatchers.IO) {
        _uiState.value = Loading

        val result = getTransactionsByPeriod(
            accountId = Constants.TEST_ACCOUNT_ID,
            startDate = getCurrentDate(),
            endDate = getCurrentDate()
        )

        result
            .onSuccess { data ->
                _uiState.value = Content(
                    expenses = data.sortedByDescending { it.transactionTime }
                        .map { mapper.map(it) },
                    totalAmount = mapper.calculateTotalAmount(data)
                )
            }
            .onFailure { error -> showError(error) }
    }

    private fun showError(t: Throwable) {
        val res = (t as? AppError)?.messageResId ?: R.string.unknown_error
        _uiState.value = Error(messageResId = res)
    }
}