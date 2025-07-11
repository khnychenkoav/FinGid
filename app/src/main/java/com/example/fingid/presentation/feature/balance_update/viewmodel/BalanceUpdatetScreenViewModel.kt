package com.example.fingid.presentation.feature.balance_update.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fingid.R
import com.example.fingid.data.remote.api.AppError
import com.example.fingid.domain.usecases.GetAccountUseCase
import com.example.fingid.domain.usecases.UpdateAccountUseCase
import com.example.fingid.presentation.feature.balance.mapper.AccountToBalanceMapper
import com.example.fingid.presentation.feature.balance_update.mapper.AccountToBalanceDetailedMapper
import com.example.fingid.presentation.feature.balance_update.model.CurrencyItem
import com.example.fingid.presentation.feature.balance_update.viewmodel.BalanceUpdateUiState.Content
import com.example.fingid.presentation.feature.balance_update.viewmodel.BalanceUpdateUiState.Error
import com.example.fingid.presentation.feature.balance_update.viewmodel.BalanceUpdateUiState.Loading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface BalanceUpdateUiState {

    data object Loading : BalanceUpdateUiState


    data class Content(
        val form: BalanceUpdateForm = BalanceUpdateForm(),
        val visibleModal: BalanceUpdateModal? = null,
        val snackbar: BalanceUpdateSnackbar? = null
    ) : BalanceUpdateUiState {
        val isSaveEnabled: Boolean
            get() =
                form.amount.toIntOrNull() != null && form.name.isNotEmpty()
    }

    data class Error(@StringRes val messageResId: Int) : BalanceUpdateUiState
}

sealed interface BalanceUpdateEvent {
    data class ShowSnackBar(@StringRes val messageResId: Int) : BalanceUpdateEvent
    data object NavigateBack : BalanceUpdateEvent
}


class BalanceUpdateScreenViewModel @Inject constructor(
    private val getAccount: GetAccountUseCase,
    private val updateBalance: UpdateAccountUseCase,
    private val mapper: AccountToBalanceDetailedMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow<BalanceUpdateUiState>(Loading)
    val uiState: StateFlow<BalanceUpdateUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<BalanceUpdateEvent>()
    val events: SharedFlow<BalanceUpdateEvent> = _events

    private fun contentOrNull() = _uiState.value as? Content


    private fun updateContent(transform: (Content) -> Content) {
        _uiState.update { ui ->
            if (ui is Content) transform(ui) else ui
        }
    }


    fun init(balanceId: String) = viewModelScope.launch {
        _uiState.value = Loading

        val result = getAccount(accountId = balanceId.toInt())

        result
            .onSuccess { domain ->
                val data = mapper.map(domain)
                _uiState.value = Content(
                    form = BalanceUpdateForm(
                        name = data.name,
                        amount = data.amount,
                        currency = CurrencyItem.items.first {
                            it.currencyCode == data.currencyCode
                        }
                    )
                )
            }
            .onFailure { error -> showError(error) }
    }


    fun onFieldChanged(field: BalanceUpdateField, value: Any) {
        updateContent { content ->
            val f = content.form
            val newForm = when (field) {
                BalanceUpdateField.NAME -> f.copy(name = value as String)
                BalanceUpdateField.AMOUNT -> f.copy(amount = value as String)
                BalanceUpdateField.CURRENCY -> f.copy(currency = value as CurrencyItem)
            }
            content.copy(form = newForm)
        }
    }

    fun closeModal() = _uiState.update { (it as Content).copy(visibleModal = null) }
    fun openModal(modal: BalanceUpdateModal) = _uiState.update {
        (it as Content).copy(visibleModal = modal)
    }


    fun saveBalance(balanceId: String) = viewModelScope.launch(Dispatchers.IO) {
        val content = contentOrNull() ?: return@launch

        val form = content.form

        if (!content.isSaveEnabled) {
            showSnackbar(R.string.balance_data_error_message)
            return@launch
        }

        _uiState.value = Loading

        val result = updateBalance(
            accountId = balanceId.toInt(),
            accountName = form.name,
            accountBalance = form.amount.toInt(),
            accountCurrency = form.currency?.currencyCode ?: "RUB"
        )

        result
            .onSuccess { _events.emit(BalanceUpdateEvent.NavigateBack) }
            .onFailure {
                _uiState.value = content
                showSnackbar(R.string.error_message)
            }
    }

    private fun showSnackbar(@StringRes resId: Int) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _events.emit(BalanceUpdateEvent.ShowSnackBar(resId))
        }
    }

    private fun showError(t: Throwable) {
        val res = (t as? AppError)?.messageResId ?: R.string.unknown_error
        _uiState.value = Error(messageResId = res)
    }
}

enum class BalanceUpdateField {
    NAME,
    AMOUNT,
    CURRENCY
}

data class BalanceUpdateForm(
    val name: String = "",
    val amount: String = "",
    val currency: CurrencyItem? = null
)

sealed interface BalanceUpdateModal {
    data object CurrencyPicker : BalanceUpdateModal
}

sealed interface BalanceUpdateSnackbar {
    data class Error(@StringRes val messageResId: Int) : BalanceUpdateSnackbar
}