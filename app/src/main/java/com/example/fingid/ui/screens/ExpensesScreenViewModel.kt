package com.example.fingid.ui.screens

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fingid.domain.model.Transaction
import com.example.fingid.domain.usecase.account.GetAccountUseCase
import com.example.fingid.domain.usecase.transaction.GetTransactionsUseCase
import com.example.fingid.ui.commonitems.UiState
import com.example.fingid.ui.commonitems.isNetworkAvailable
import com.example.fingid.ui.commonitems.retryWithBackoff
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExpensesScreenViewModel(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getAccountUseCase: GetAccountUseCase
) : ViewModel() {

    private val _expenseList = MutableLiveData<UiState<List<Transaction>>>()
    val expenseList: LiveData<UiState<List<Transaction>>> get() = _expenseList

    fun loadTransactions(from: String, to: String, context: Context) {
        viewModelScope.launch {
            _expenseList.value = UiState.Loading
            if (!isNetworkAvailable(context)) {
                _expenseList.value = UiState.Error("Нет подключения к сети")
                return@launch
            }

            try {
                val accounts = withContext(Dispatchers.IO) {
                    retryWithBackoff { getAccountUseCase() }
                }
                if (accounts.isEmpty()) {
                    _expenseList.value = UiState.Error("Нет доступных счетов")
                    return@launch
                }

                val id = accounts.first().id
                val transactions = withContext(Dispatchers.IO) {
                    retryWithBackoff { getTransactionsUseCase(id, from, to) }
                }

                val expenses = transactions.filter { !it.isIncome }
                _expenseList.value = UiState.Success(expenses)

            } catch (e: Exception) {
                e.printStackTrace()
                _expenseList.value = UiState.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }
}