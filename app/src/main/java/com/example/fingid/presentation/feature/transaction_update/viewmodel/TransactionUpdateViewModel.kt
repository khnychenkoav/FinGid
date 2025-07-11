package com.example.fingid.presentation.feature.transaction_update.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fingid.R
import com.example.fingid.core.utils.Constants
import com.example.fingid.core.utils.formatHumanDateToIso
import com.example.fingid.core.utils.formatIsoDateToHuman
import com.example.fingid.core.utils.formatLongToHumanDate
import com.example.fingid.core.utils.getCurrentDateIso
import com.example.fingid.core.utils.getCurrentTime
import com.example.fingid.data.remote.api.AppError
import com.example.fingid.domain.usecases.DeleteTransactionUseCase
import com.example.fingid.domain.usecases.GetCategoriesByTypeUseCase
import com.example.fingid.domain.usecases.GetTransactionUseCase
import com.example.fingid.domain.usecases.UpdateTransactionUseCase
import com.example.fingid.presentation.feature.categories.mapper.CategoryToCategoryUiMapper
import com.example.fingid.presentation.feature.categories.model.CategoryUiModel
import com.example.fingid.presentation.feature.transaction_update.mapper.TransactionResponseToTransactionDetailed
import com.example.fingid.presentation.feature.transaction_update.viewmodel.TransactionUpdateUiState.Content
import com.example.fingid.presentation.feature.transaction_update.viewmodel.TransactionUpdateUiState.Error
import com.example.fingid.presentation.feature.transaction_update.viewmodel.TransactionUpdateUiState.Loading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface TransactionUpdateUiState {

    data object Loading : TransactionUpdateUiState

    data class Content(
        val form: TransactionUpdateForm = TransactionUpdateForm(),
        val categories: List<CategoryUiModel> = emptyList(),
        val visibleModal: TransactionUpdateModal? = null,
        val snackbar: TransactionUpdateSnackbar? = null
    ) : TransactionUpdateUiState {
        val isSaveEnabled: Boolean get() = form.amount.toIntOrNull() != null
    }

    data class Error(@StringRes val messageResId: Int) : TransactionUpdateUiState
}

sealed interface TransactionUpdateEvent {
    data class ShowSnackBar(@StringRes val messageResId: Int) : TransactionUpdateEvent
    data object NavigateBack : TransactionUpdateEvent
}

class TransactionUpdateViewModel @Inject constructor(
    private val getCategoriesByType: GetCategoriesByTypeUseCase,
    private val categoryMapper: CategoryToCategoryUiMapper,
    private val getTransaction: GetTransactionUseCase,
    private val transactionMapper: TransactionResponseToTransactionDetailed,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<TransactionUpdateUiState>(Loading)
    val uiState: StateFlow<TransactionUpdateUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<TransactionUpdateEvent>()
    val events: SharedFlow<TransactionUpdateEvent> = _events

    private fun contentOrNull() = _uiState.value as? Content


    private fun updateContent(transform: (Content) -> Content) {
        _uiState.update { ui ->
            if (ui is Content) transform(ui) else ui
        }
    }


    fun init(transactionId: Int, isIncome: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        _uiState.value = Loading

        val transactionDef = async { getTransaction(transactionId) }
        val categoriesDef = async { getCategoriesByType(isIncome) }

        val transactionDomain = transactionDef.await().getOrElse { return@launch showError(it) }
        val categoriesDomain = categoriesDef.await().getOrElse { return@launch showError(it) }

        val transaction = transactionMapper.map(transactionDomain)
        val categories = categoriesDomain.map { categoryMapper.map(it) }

        _uiState.value = Content(
            form = TransactionUpdateForm(
                id = transaction.id,
                balance = transaction.accountName,
                selectedCategory = categories.first { category ->
                    category.id == transaction.categoryId
                },
                date = formatIsoDateToHuman(transaction.date),
                time = transaction.time,
                amount = transaction.amount,
                comment = transaction.comment.orEmpty(),
                isIncome = transaction.isIncome
            ),
            categories = categories
        )
    }


    fun onFieldChanged(field: TransactionUpdateField, value: Any) {
        updateContent { content ->
            val f = content.form
            val newForm = when (field) {
                TransactionUpdateField.BALANCE -> f.copy(balance = value as String)
                TransactionUpdateField.CATEGORY -> f.copy(selectedCategory = value as CategoryUiModel)
                TransactionUpdateField.AMOUNT -> f.copy(amount = value as String)
                TransactionUpdateField.COMMENT -> f.copy(comment = value as String)
                TransactionUpdateField.DATE -> f.copy(date = formatLongToHumanDate(value as Long))
                TransactionUpdateField.TIME -> f.copy(time = (value as Pair<Int, Int>).toTimeString())
            }
            content.copy(form = newForm)
        }
    }

    fun closeModal() = _uiState.update { (it as Content).copy(visibleModal = null) }
    fun openModal(modal: TransactionUpdateModal) = _uiState.update {
        (it as Content).copy(visibleModal = modal)
    }

    private fun showSnackbar(@StringRes resId: Int) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _events.emit(TransactionUpdateEvent.ShowSnackBar(resId))
        }
    }


    fun saveTransaction(transactionId: Int) = viewModelScope.launch(Dispatchers.IO) {
        val content = contentOrNull() ?: return@launch

        val form = content.form

        if (!content.isSaveEnabled) {
            showSnackbar(R.string.amount_error_message)
            return@launch
        }

        _uiState.value = Loading

        val result = updateTransactionUseCase(
            transactionId = transactionId,
            accountId = Constants.TEST_ACCOUNT_ID,
            categoryId = form.selectedCategory?.id ?: 0,
            amount = form.amount,
            transactionDate = formatHumanDateToIso(form.date),
            transactionTime = form.time,
            comment = form.comment
        )

        result
            .onSuccess { _events.emit(TransactionUpdateEvent.NavigateBack) }
            .onFailure {
                _uiState.value = content
                showSnackbar(R.string.error_message)
            }
    }


    fun deleteTransaction(transactionId: Int) = viewModelScope.launch(Dispatchers.IO) {
        val content = contentOrNull() ?: return@launch

        _uiState.value = Loading

        val result = deleteTransactionUseCase(transactionId)

        result
            .onSuccess { _events.emit(TransactionUpdateEvent.NavigateBack) }
            .onFailure {
                _uiState.value = content
                showSnackbar(R.string.error_message)
            }
    }

    private fun showError(t: Throwable) {
        val res = (t as? AppError)?.messageResId ?: R.string.unknown_error
        _uiState.value = Error(messageResId = res)
    }
}

enum class TransactionUpdateField {
    BALANCE,
    CATEGORY,
    AMOUNT,
    COMMENT,
    DATE,
    TIME
}

data class TransactionUpdateForm(
    val id: Int = 0,
    val balance: String = "",
    val currencySymbol: String = "$",
    val selectedCategory: CategoryUiModel? = null,
    val date: String = getCurrentDateIso(),
    val time: String = getCurrentTime(),
    val amount: String = "",
    val comment: String = "",
    val isIncome: Boolean = true
)

sealed interface TransactionUpdateModal {
    data object DatePicker : TransactionUpdateModal
    data object TimePicker : TransactionUpdateModal
    data object CategoryPicker : TransactionUpdateModal
}

sealed interface TransactionUpdateSnackbar {
    data class Error(@StringRes val messageResId: Int) : TransactionUpdateSnackbar
}

private fun Pair<Int, Int>.toTimeString(): String = "%02d:%02d".format(first, second)