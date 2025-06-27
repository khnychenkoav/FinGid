package com.example.fingid.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fingid.domain.model.Account
import com.example.fingid.domain.usecase.account.GetAccountUseCase
import com.example.fingid.domain.usecase.account.UpdateAccountsUseCase
import com.example.fingid.ui.commonitems.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditAccountViewModel(
    internal val accountIdToEdit: Long?,
    private val getAccountUseCase: GetAccountUseCase,
    private val updateAccountsUseCase: UpdateAccountsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<String>>(UiState.Loading)
    val uiState: StateFlow<UiState<String>> = _uiState

    private val _accountDetails = MutableStateFlow<Account?>(null)
    val accountDetails: StateFlow<Account?> = _accountDetails

    init {
        accountIdToEdit?.let {
            fetchAccountDetails(it)
        } ?: run {
            _uiState.value = UiState.Success("Ready to create new account")
        }
    }

    private fun fetchAccountDetails(id: Long) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val accounts = getAccountUseCase()
                val account = accounts.find { it.id.toLong() == id }
                if (account != null) {
                    _accountDetails.value = account
                    _uiState.value = UiState.Success("Account data loaded")
                } else {
                    _uiState.value = UiState.Error("Счет не найден")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Ошибка загрузки данных")
            }
        }
    }

    fun saveAccount(name: String, balanceStr: String, currency: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val balance = balanceStr.toDoubleOrNull()
            if (name.isBlank() || balance == null || currency.isBlank()) {
                _uiState.value = UiState.Error("Все поля должны быть заполнены.")
                return@launch
            }

            try {
                val accountToSave = Account(
                    id = accountIdToEdit?.toInt() ?: 0,
                    name = name,
                    balance = balance,
                    currency = currency
                )

                updateAccountsUseCase(listOf(accountToSave))
                _uiState.value = UiState.Success("Счет сохранен")

            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Ошибка сохранения")
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = UiState.Success("Счет удален (имитация)")
        }
    }

    fun resetState() {
        _uiState.value = UiState.Success("Idle")
    }
}