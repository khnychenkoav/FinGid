package com.example.fingid.presentation.feature.balance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fingid.R
import com.example.fingid.core.utils.Constants
import com.example.fingid.data.remote.api.AppError
import com.example.fingid.domain.model.AccountDomain
import com.example.fingid.domain.usecases.GetAccountUseCase
import com.example.fingid.presentation.feature.balance.mapper.AccountToBalanceMapper
import com.example.fingid.presentation.feature.balance.model.BalanceUiModel
import com.example.fingid.presentation.feature.balance.viewmodel.BalanceScreenState.Error
import com.example.fingid.presentation.feature.balance.viewmodel.BalanceScreenState.Loading
import com.example.fingid.presentation.feature.balance.viewmodel.BalanceScreenState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface BalanceScreenState {
    data object Loading : BalanceScreenState
    data class Error(val messageResId: Int, val retryAction: () -> Unit) : BalanceScreenState
    data class Success(val balance: BalanceUiModel) : BalanceScreenState
}


@HiltViewModel
class BalanceScreenViewModel @Inject constructor(
    private val getAccount: GetAccountUseCase,
    private val mapper: AccountToBalanceMapper
) : ViewModel() {

    private val _screenState = MutableStateFlow<BalanceScreenState>(Loading)
    val screenState: StateFlow<BalanceScreenState> = _screenState.asStateFlow()

    private val _accountId = MutableStateFlow(Constants.TEST_ACCOUNT_ID)

    init {
        loadBalanceInfo()
    }


    private fun loadBalanceInfo() {
        _screenState.value = Loading
        viewModelScope.launch(Dispatchers.IO) {
            handleBalanceResult(getAccount(accountId = _accountId.value))
        }
    }


    private fun handleBalanceResult(result: Result<AccountDomain>) {
        result
            .onSuccess { data -> handleSuccess(mapper.map(data)) }
            .onFailure { error -> handleError(error) }
    }

    private fun handleSuccess(data: BalanceUiModel) {
        _screenState.value = Success(data)
    }

    private fun handleError(error: Throwable) {
        val messageResId = (error as? AppError)?.messageResId ?: R.string.unknown_error
        _screenState.value = Error(
            messageResId = messageResId,
            retryAction = { loadBalanceInfo() }
        )
    }

    fun getAccountId(): Int {
        return _accountId.value
    }
}
