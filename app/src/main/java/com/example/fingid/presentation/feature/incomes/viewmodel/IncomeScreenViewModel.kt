package com.example.fingid.presentation.feature.incomes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fingid.R
import com.example.fingid.core.utils.Constants
import com.example.fingid.core.utils.getCurrentDate
import com.example.fingid.data.remote.api.AppError
import com.example.fingid.domain.model.TransactionDomain
import com.example.fingid.domain.usecases.GetIncomesByPeriodUseCase
import com.example.fingid.presentation.feature.incomes.mapper.TransactionToIncomeMapper
import com.example.fingid.presentation.feature.incomes.model.IncomeUiModel
import com.example.fingid.presentation.feature.incomes.viewmodel.IncomeScreenState.Error
import com.example.fingid.presentation.feature.incomes.viewmodel.IncomeScreenState.Loading
import com.example.fingid.presentation.feature.incomes.viewmodel.IncomeScreenState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface IncomeScreenState {
    data object Loading : IncomeScreenState
    data class Error(val messageResId: Int, val retryAction: () -> Unit) : IncomeScreenState
    data object Empty : IncomeScreenState
    data class Success(
        val incomes: List<IncomeUiModel>,
        val totalAmount: String
    ) : IncomeScreenState
}


@HiltViewModel
class IncomeScreenViewModel @Inject constructor(
    private val getTransactionsByPeriodUseCase: GetIncomesByPeriodUseCase,
    private val mapper: TransactionToIncomeMapper
) : ViewModel() {

    private val _screenState = MutableStateFlow<IncomeScreenState>(Loading)
    val screenState: StateFlow<IncomeScreenState> = _screenState.asStateFlow()

    init {
        loadIncomes()
    }


    private fun loadIncomes() {
        _screenState.value = Loading
        viewModelScope.launch(Dispatchers.IO) {
            handleIncomesResult(
                getTransactionsByPeriodUseCase(
                    accountId = Constants.TEST_ACCOUNT_ID,
                    startDate = getCurrentDate(),
                    endDate = getCurrentDate()
                )
            )
        }
    }


    private fun handleIncomesResult(result: Result<List<TransactionDomain>>) {
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
        data: List<IncomeUiModel>,
        totalAmount: String
    ) {
        _screenState.value = if (data.isEmpty()) {
            IncomeScreenState.Empty
        } else {
            Success(
                incomes = data,
                totalAmount = totalAmount
            )
        }
    }

    private fun handleError(error: Throwable) {
        val messageResId = (error as? AppError)?.messageResId ?: R.string.unknown_error
        _screenState.value = Error(
            messageResId = messageResId,
            retryAction = { loadIncomes() }
        )
    }
}