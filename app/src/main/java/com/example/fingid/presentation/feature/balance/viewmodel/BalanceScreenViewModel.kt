package com.example.fingid.presentation.feature.balance.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fingid.R
import com.example.fingid.core.utils.Constants
import com.example.fingid.data.remote.api.AppError
import com.example.fingid.domain.usecases.GetAccountUseCase
import com.example.fingid.presentation.feature.balance.mapper.AccountToBalanceMapper
import com.example.fingid.presentation.feature.balance.model.BalanceUiModel
import com.example.fingid.presentation.feature.balance.viewmodel.BalanceUiState.Content
import com.example.fingid.presentation.feature.balance.viewmodel.BalanceUiState.Error
import com.example.fingid.presentation.feature.balance.viewmodel.BalanceUiState.Loading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface BalanceUiState {

    data object Loading : BalanceUiState

    data class Content(val balance: BalanceUiModel) : BalanceUiState

    data class Error(@StringRes val messageResId: Int) : BalanceUiState
}


class BalanceScreenViewModel @Inject constructor(
    private val getAccount: GetAccountUseCase,
    private val mapper: AccountToBalanceMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow<BalanceUiState>(Loading)
    val uiState: StateFlow<BalanceUiState> = _uiState.asStateFlow()

    private val accountId = MutableStateFlow(Constants.TEST_ACCOUNT_ID)


    fun init() = viewModelScope.launch(Dispatchers.IO) {
        _uiState.value = Loading

        val result = getAccount(accountId = accountId.value)

        result
            .onSuccess { data -> _uiState.value = Content(mapper.map(data)) }
            .onFailure { error -> showError(error) }
    }

    private fun showError(t: Throwable) {
        val res = (t as? AppError)?.messageResId ?: R.string.unknown_error
        _uiState.value = Error(messageResId = res)
    }

    fun getAccountId(): Int = accountId.value
}
