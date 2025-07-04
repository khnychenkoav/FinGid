package com.example.fingid.presentation.feature.balance_edit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fingid.R
import com.example.fingid.data.remote.api.AppError
import com.example.fingid.domain.model.AccountDomain
import com.example.fingid.domain.usecases.GetAccountUseCase
import com.example.fingid.domain.usecases.UpdateAccountUseCase
import com.example.fingid.presentation.feature.balance.mapper.AccountToBalanceMapper
import com.example.fingid.presentation.feature.balance_edit.mapper.AccountToBalanceDetailedMapper
import com.example.fingid.presentation.feature.balance_edit.model.BalanceDetailedUiModel
import com.example.fingid.presentation.feature.balance_edit.model.CurrencyItem
import com.example.fingid.presentation.feature.balance_edit.viewmodel.BalanceEditScreenState.Error
import com.example.fingid.presentation.feature.balance_edit.viewmodel.BalanceEditScreenState.Loading
import com.example.fingid.presentation.feature.balance_edit.viewmodel.BalanceEditScreenState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface BalanceEditScreenState {
    data object Loading : BalanceEditScreenState
    data class Error(val messageResId: Int, val retryAction: () -> Unit) : BalanceEditScreenState
    data class Success(
        val name: StateFlow<String>,
        val balance: StateFlow<String>,
        val currencySymbol: StateFlow<String>
    ) : BalanceEditScreenState
}


@HiltViewModel
class BalanceEditScreenViewModel @Inject constructor(
    private val getAccount: GetAccountUseCase,
    private val updateAccount: UpdateAccountUseCase,
    private val mapper: AccountToBalanceDetailedMapper
) : ViewModel() {

    private val _accountId = MutableStateFlow(0)
    private val _screenState = MutableStateFlow<BalanceEditScreenState>(Loading)
    val screenState: StateFlow<BalanceEditScreenState> = _screenState.asStateFlow()

    private val _accountName = MutableStateFlow("")
    private val _accountBalance = MutableStateFlow("")
    private val _accountCurrencySymbol = MutableStateFlow("")
    private val _accountCurrencyCode = MutableStateFlow("")

    private val _currencySelectionSheetVisible = MutableStateFlow(false)
    val currencySelectionSheetVisible: StateFlow<Boolean> =
        _currencySelectionSheetVisible.asStateFlow()

    private val _snackbarVisible = MutableStateFlow(false)
    val snackbarVisible: StateFlow<Boolean> = _snackbarVisible.asStateFlow()

    private val _snackbarMessageId = MutableStateFlow(0)
    val snackbarMessage: StateFlow<Int> = _snackbarMessageId.asStateFlow()


    fun setAccountId(id: String) {
        _accountId.value = id.toInt().also {
            loadBalanceInfo()
        }
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

    private fun handleSuccess(data: BalanceDetailedUiModel) {
        _accountName.value = data.name
        _accountBalance.value = data.amount
        _accountCurrencyCode.value = data.currencyCode
        _accountCurrencySymbol.value = data.currencySymbol

        _screenState.value = Success(
            name = _accountName.asStateFlow(),
            balance = _accountBalance.asStateFlow(),
            currencySymbol = _accountCurrencySymbol.asStateFlow()
        )
    }

    private fun handleError(error: Throwable) {
        val messageResId = (error as? AppError)?.messageResId ?: R.string.unknown_error
        _screenState.value = Error(
            messageResId = messageResId,
            retryAction = { loadBalanceInfo() }
        )
    }

    fun updateAccountData() {
        viewModelScope.launch(Dispatchers.IO) {
            updateAccount(
                accountId = _accountId.value,
                accountName = _accountName.value,
                accountBalance = _accountBalance.value.toInt(),
                accountCurrency = _accountCurrencyCode.value
            )
        }
    }

    fun validateAccountData(): Boolean {
        if (_accountName.value.isBlank()) {
            showSnackBar(R.string.balance_name_error_message)
            return false
        }

        try {
            _accountBalance.value.toInt()
        } catch (e: NumberFormatException) {
            showSnackBar(R.string.balance_amount_error_message)
            return false
        }

        return true
    }


    fun onCurrencySelected(currency: CurrencyItem) {
        _accountCurrencyCode.value = currency.currencyCode
        _accountCurrencySymbol.value = currency.currencySymbol
    }


    fun onBalanceEdited(balance: String) {
        _accountBalance.value = balance
    }

    fun onNameEdited(name: String) {
        _accountName.value = name
    }

    fun showCurrencyBottomSheet() {
        _currencySelectionSheetVisible.value = true
    }

    fun hideCurrencyBottomSheet() {
        _currencySelectionSheetVisible.value = false
    }

    private fun showSnackBar(message: Int) {
        if (!_snackbarVisible.value) {
            _snackbarVisible.value = true
            _snackbarMessageId.value = message
            viewModelScope.launch {
                delay(4000)
                dismissSnackBar()
            }
        }
    }

    fun dismissSnackBar() {
        _snackbarVisible.value = false
    }
}