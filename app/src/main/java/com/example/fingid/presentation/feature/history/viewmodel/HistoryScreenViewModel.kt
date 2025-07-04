package com.example.fingid.presentation.feature.history.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fingid.R
import com.example.fingid.core.utils.Constants
import com.example.fingid.core.utils.formatHumanDateToIso
import com.example.fingid.core.utils.formatLongToHumanDate
import com.example.fingid.core.utils.getCurrentDateIso
import com.example.fingid.core.utils.getStartOfCurrentMonth
import com.example.fingid.data.remote.api.AppError
import com.example.fingid.domain.model.TransactionDomain
import com.example.fingid.domain.usecases.GetExpensesByPeriodUseCase
import com.example.fingid.domain.usecases.GetIncomesByPeriodUseCase
import com.example.fingid.presentation.feature.history.mapper.TransactionToTransactionUiMapper
import com.example.fingid.presentation.feature.history.model.TransactionUiModel
import com.example.fingid.presentation.feature.history.viewmodel.HistoryScreenState.Error
import com.example.fingid.presentation.feature.history.viewmodel.HistoryScreenState.Loading
import com.example.fingid.presentation.feature.history.viewmodel.HistoryScreenState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface HistoryScreenState {
    data object Loading : HistoryScreenState
    data class Error(val messageResId: Int, val retryAction: () -> Unit) : HistoryScreenState
    data object Empty : HistoryScreenState
    data class Success(
        val transactions: List<TransactionUiModel>,
        val totalAmount: String
    ) : HistoryScreenState
}


@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HistoryScreenViewModel @Inject constructor(
    private val getExpensesByPeriodUseCase: GetExpensesByPeriodUseCase,
    private val getIncomesByPeriodUseCase: GetIncomesByPeriodUseCase,
    private val mapper: TransactionToTransactionUiMapper
) : ViewModel() {

    private val _historyTransactionsType = MutableStateFlow(false)

    private val _historyStartDate = MutableStateFlow(getStartOfCurrentMonth())
    val historyStartDate: StateFlow<String> = _historyStartDate.asStateFlow()

    private val _historyEndDate = MutableStateFlow(getCurrentDateIso())
    val historyEndDate: StateFlow<String> = _historyEndDate.asStateFlow()

    private val _screenState = MutableStateFlow<HistoryScreenState>(Loading)
    val screenState: StateFlow<HistoryScreenState> = _screenState.asStateFlow()

    private val _editingDateType = mutableStateOf<DateType?>(null)

    private val _showDatePickerModal = MutableStateFlow(false)
    val showDatePickerModal: StateFlow<Boolean> = _showDatePickerModal.asStateFlow()

    init {
        loadHistory()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadHistory() {
        _screenState.value = Loading
        viewModelScope.launch(Dispatchers.IO) {
            if (_historyTransactionsType.value) {
                getIncomesByPeriodUseCase(
                    accountId = Constants.TEST_ACCOUNT_ID,
                    startDate = formatHumanDateToIso(_historyStartDate.value),
                    endDate = formatHumanDateToIso(_historyEndDate.value)
                )
            } else {
                getExpensesByPeriodUseCase(
                    accountId = Constants.TEST_ACCOUNT_ID,
                    startDate = formatHumanDateToIso(_historyStartDate.value),
                    endDate = formatHumanDateToIso(_historyEndDate.value)
                )
            }
        }
    }


    private fun handleTransactionsResult(result: Result<List<TransactionDomain>>) {
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
        data: List<TransactionUiModel>,
        totalAmount: String
    ) {
        _screenState.value = if (data.isEmpty()) {
            HistoryScreenState.Empty
        } else {
            Success(
                transactions = data,
                totalAmount = totalAmount
            )
        }
    }

    private fun handleError(error: Throwable) {
        val messageResId = (error as? AppError)?.messageResId ?: R.string.unknown_error
        _screenState.value = Error(
            messageResId = messageResId,
            retryAction = { loadHistory() }
        )
    }


    fun setHistoryTransactionsType(isIncome: Boolean) {
        _historyTransactionsType.value = isIncome
    }


    fun showDatePickerModal(type: DateType) {
        _showDatePickerModal.value = true
        _editingDateType.value = type
    }


    fun confirmDateSelection(date: Long) {
        when (_editingDateType.value as DateType) {
            DateType.START -> _historyStartDate.value = formatLongToHumanDate(date)
            DateType.END -> _historyEndDate.value = formatLongToHumanDate(date)
        }
        _showDatePickerModal.value = false
        loadHistory()
    }

    fun onDismissDatePicker() {
        _showDatePickerModal.value = false
    }
}

enum class DateType { START, END }
