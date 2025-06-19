package com.example.fingid.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fingid.data.remote.dto.AccountCreateRequestDto
import com.example.fingid.data.remote.dto.AccountUpdateRequestDto
import com.example.fingid.data.repository.FinanceRepository
import com.example.fingid.data.repository.NetworkResult
import com.example.fingid.domain.entities.Account
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal

sealed class EditAccountUiState {
    object Idle : EditAccountUiState()
    object Loading : EditAccountUiState()
    data class Success(val account: Account? = null, val message: String? = null) : EditAccountUiState()
    data class Error(val message: String) : EditAccountUiState()
}

class EditAccountViewModel(
    internal val accountIdToEdit: Long? = null,
    private val repository: FinanceRepository = FinanceRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<EditAccountUiState>(EditAccountUiState.Idle)
    val uiState: StateFlow<EditAccountUiState> = _uiState.asStateFlow()

    private val _accountDetails = MutableStateFlow<Account?>(null)
    val accountDetails: StateFlow<Account?> = _accountDetails.asStateFlow()

    init {
        accountIdToEdit?.let {
            fetchAccountDetails(it)
        }
    }

    private fun fetchAccountDetails(id: Long) {
        viewModelScope.launch {
            _uiState.value = EditAccountUiState.Loading
            when (val result = repository.getAccount(id)) {
                is NetworkResult.Success -> {
                    _accountDetails.value = result.data
                    _uiState.value = EditAccountUiState.Success(account = result.data)
                }
                is NetworkResult.Error -> {
                    _uiState.value = EditAccountUiState.Error(result.message)
                }
            }
        }
    }

    fun saveAccount(name: String, balanceStr: String, currency: String) {
        viewModelScope.launch {
            _uiState.value = EditAccountUiState.Loading
            val balance = balanceStr.replace(Regex("[^0-9.-]"), "")
            if (name.isBlank() || balance.isBlank() || currency.isBlank()) {
                _uiState.value = EditAccountUiState.Error("Все поля должны быть заполнены.")
                return@launch
            }
            try {
                BigDecimal(balance)
            } catch (e: NumberFormatException) {
                _uiState.value = EditAccountUiState.Error("Некорректный формат баланса.")
                return@launch
            }


            val result = if (accountIdToEdit == null) {
                val request = AccountCreateRequestDto(name, balance, currency)
                repository.createAccount(request)
            } else {
                val request = AccountUpdateRequestDto(name, balance, currency)
                repository.updateAccount(accountIdToEdit, request)
            }

            when (result) {
                is NetworkResult.Success -> _uiState.value = EditAccountUiState.Success(account = result.data, message = "Счет сохранен")
                is NetworkResult.Error -> _uiState.value = EditAccountUiState.Error(result.message)
            }
        }
    }

    fun deleteAccount() {
        accountIdToEdit?.let { id ->
            viewModelScope.launch {
                _uiState.value = EditAccountUiState.Loading
                when (val result = repository.deleteAccount(id)) {
                    is NetworkResult.Success -> _uiState.value = EditAccountUiState.Success(message = "Счет удален")
                    is NetworkResult.Error -> _uiState.value = EditAccountUiState.Error(result.message)
                }
            }
        } ?: run {
            _uiState.value = EditAccountUiState.Error("Невозможно удалить: счет не существует.")
        }
    }
    fun resetState() {
        _uiState.value = EditAccountUiState.Idle
    }
}