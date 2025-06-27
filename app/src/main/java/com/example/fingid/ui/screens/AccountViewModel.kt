package com.example.fingid.ui.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fingid.domain.model.Account
import com.example.fingid.domain.usecase.account.GetAccountUseCase
import com.example.fingid.ui.commonitems.UiState
import com.example.fingid.ui.commonitems.isNetworkAvailable
import com.example.fingid.ui.commonitems.retryWithBackoff
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AccountViewModel(
    private val getAccountUseCase: GetAccountUseCase,
    private val accountId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Account>>(UiState.Loading)
    val uiState: StateFlow<UiState<Account>> = _uiState

    fun fetchAccountDetails(context: Context) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            if (!isNetworkAvailable(context)) {
                _uiState.value = UiState.Error("Отсутствует подключение к интернету")
                return@launch
            }
            try {
                val accounts = withContext(Dispatchers.IO) {
                    retryWithBackoff { getAccountUseCase() }
                }
                val account = accounts.find { it.id.toLong() == accountId }
                if (account != null) {
                    _uiState.value = UiState.Success(account)
                } else {
                    _uiState.value = UiState.Error("Счет с ID $accountId не найден")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = UiState.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }
}