package com.example.fingid.presentation.feature.incomes.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fingid.R
import com.example.fingid.core.utils.Constants
import com.example.fingid.core.utils.getCurrentDate
import com.example.fingid.data.remote.api.AppError
import com.example.fingid.domain.usecases.GetIncomesByPeriodUseCase
import com.example.fingid.presentation.feature.incomes.mapper.TransactionToIncomeMapper
import com.example.fingid.presentation.feature.incomes.model.IncomeUiModel
import com.example.fingid.presentation.feature.incomes.viewmodel.IncomesUiState.Content
import com.example.fingid.presentation.feature.incomes.viewmodel.IncomesUiState.Empty
import com.example.fingid.presentation.feature.incomes.viewmodel.IncomesUiState.Error
import com.example.fingid.presentation.feature.incomes.viewmodel.IncomesUiState.Loading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface IncomesUiState {

    data object Loading : IncomesUiState


    data class Content(
        val incomes: List<IncomeUiModel>,
        val totalAmount: String
    ) : IncomesUiState

    data object Empty : IncomesUiState

    data class Error(@StringRes val messageResId: Int) : IncomesUiState
}


class IncomeScreenViewModel @Inject constructor(
    private val getTransactionsByPeriod: GetIncomesByPeriodUseCase,
    private val mapper: TransactionToIncomeMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow<IncomesUiState>(Loading)
    val uiState: StateFlow<IncomesUiState> = _uiState.asStateFlow()


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
                    incomes = data.sortedByDescending { it.transactionTime }
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