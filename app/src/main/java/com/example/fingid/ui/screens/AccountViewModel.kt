package com.example.fingid.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fingid.data.repository.FinanceRepository
import com.example.fingid.data.repository.NetworkResult
import com.example.fingid.domain.entities.Account
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AccountUiState {
    object Loading : AccountUiState()
    data class Success(val account: Account) : AccountUiState()
    data class Error(val message: String) : AccountUiState()
    object Idle : AccountUiState()
}

class AccountViewModel(
    private val accountId: Long,
    private val repository: FinanceRepository = FinanceRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<AccountUiState>(AccountUiState.Idle)
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    init {
        if (accountId != 0L && accountId != -1L) {
            fetchAccountDetails()
        } else {
            _uiState.value = AccountUiState.Error("Неверный ID счета для загрузки.")
        }
    }

    fun fetchAccountDetails() {
        viewModelScope.launch {
            _uiState.value = AccountUiState.Loading
            when (val result = repository.getAccount(accountId)) {
                is NetworkResult.Success -> {
                    _uiState.value = AccountUiState.Success(result.data)
                }
                is NetworkResult.Error -> {
                    _uiState.value = AccountUiState.Error(result.message)
                }
            }
        }
    }

    // TODO: добавить методы для загрузки данных для графика,
}