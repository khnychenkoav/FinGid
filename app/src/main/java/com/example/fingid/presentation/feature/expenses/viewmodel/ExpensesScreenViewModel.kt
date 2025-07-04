package com.example.fingid.presentation.feature.expenses.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fingid.R
import com.example.fingid.core.utils.Constants
import com.example.fingid.core.utils.getCurrentDate
import com.example.fingid.data.remote.api.AppError
import com.example.fingid.domain.model.TransactionDomain
import com.example.fingid.domain.usecases.GetExpensesByPeriodUseCase
import com.example.fingid.presentation.feature.balance.model.BalanceUiModel
import com.example.fingid.presentation.feature.expenses.mapper.TransactionToExpenseMapper
import com.example.fingid.presentation.feature.expenses.model.ExpenseUiModel
import com.example.fingid.presentation.feature.expenses.viewmodel.ExpensesScreenState.Error
import com.example.fingid.presentation.feature.expenses.viewmodel.ExpensesScreenState.Loading
import com.example.fingid.presentation.feature.expenses.viewmodel.ExpensesScreenState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface ExpensesScreenState {
    data object Loading : ExpensesScreenState
    data class Error(val messageResId: Int, val retryAction: () -> Unit) : ExpensesScreenState
    data object Empty : ExpensesScreenState
    data class Success(
        val expenses: List<ExpenseUiModel>,
        val totalAmount: String
    ) : ExpensesScreenState
}


@HiltViewModel
class ExpensesScreenViewModel @Inject constructor(
    private val getTransactionsByPeriod: GetExpensesByPeriodUseCase,
    private val mapper: TransactionToExpenseMapper
) : ViewModel() {

    private val _screenState = MutableStateFlow<ExpensesScreenState>(Loading)
    val screenState: StateFlow<ExpensesScreenState> = _screenState.asStateFlow()

    init {
        loadExpenses()
    }


    private fun loadExpenses() {
        _screenState.value = Loading
        viewModelScope.launch(Dispatchers.IO) {
            handleExpensesResult(
                getTransactionsByPeriod(
                    accountId = Constants.TEST_ACCOUNT_ID,
                    startDate = getCurrentDate(),
                    endDate = getCurrentDate()
                )
            )
        }
    }


    private fun handleExpensesResult(result: Result<List<TransactionDomain>>) {
        result
            .onSuccess { data ->
                handleSuccess(
                    data = data.sortedByDescending { it.transactionTime }.map { mapper.map(it) },
                    totalAmount = mapper.calculateTotalAmount(data)
                )
            }
            .onFailure { error -> handleError(error) }
    }

    private fun handleSuccess(
        data: List<ExpenseUiModel>,
        totalAmount: String
    ) {
        _screenState.value = if (data.isEmpty()) {
            ExpensesScreenState.Empty
        } else {
            Success(
                expenses = data,
                totalAmount = totalAmount
            )
        }
    }

    private fun handleError(error: Throwable) {
        val messageResId = (error as? AppError)?.messageResId ?: R.string.unknown_error
        _screenState.value = Error(
            messageResId = messageResId,
            retryAction = { loadExpenses() }
        )
    }
}
