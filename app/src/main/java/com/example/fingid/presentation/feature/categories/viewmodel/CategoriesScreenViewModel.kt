package com.example.fingid.presentation.feature.categories.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fingid.R
import com.example.fingid.data.remote.api.AppError
import com.example.fingid.domain.model.CategoryDomain
import com.example.fingid.domain.usecases.GetCategoriesUseCase
import com.example.fingid.presentation.feature.categories.mapper.CategoryToIncomeCategoryMapper
import com.example.fingid.presentation.feature.categories.model.IncomeCategoryUiModel
import com.example.fingid.presentation.feature.categories.viewmodel.CategoriesScreenState.Error
import com.example.fingid.presentation.feature.categories.viewmodel.CategoriesScreenState.Loading
import com.example.fingid.presentation.feature.categories.viewmodel.CategoriesScreenState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface CategoriesScreenState {
    data object Loading : CategoriesScreenState
    data class Error(val messageResId: Int, val retryAction: () -> Unit) : CategoriesScreenState
    data object Empty : CategoriesScreenState
    data object SearchEmpty : CategoriesScreenState
    data class Success(val categories: List<IncomeCategoryUiModel>) : CategoriesScreenState
}


@HiltViewModel
class CategoriesScreenViewModel @Inject constructor(
    private val getCategories: GetCategoriesUseCase,
    private val mapper: CategoryToIncomeCategoryMapper
) : ViewModel() {

    private val _screenState =
        MutableStateFlow<CategoriesScreenState>(Loading)
    val screenState: StateFlow<CategoriesScreenState> = _screenState.asStateFlow()

    private var cachedCategories: List<IncomeCategoryUiModel> = emptyList()

    private val _searchRequest = MutableStateFlow("")
    val searchRequest: StateFlow<String> = _searchRequest

    init {
        loadCategories()
    }


    private fun loadCategories() {
        _screenState.value = Loading
        viewModelScope.launch(Dispatchers.IO) {
            handleCategoriesResult(getCategories())
        }
    }

    private fun handleCategoriesResult(result: Result<List<CategoryDomain>>) {
        result
            .onSuccess { data -> handleSuccess(data.map { mapper.map(it) }) }
            .onFailure { error -> handleError(error) }
    }

    private fun handleSuccess(data: List<IncomeCategoryUiModel>) {
        cachedCategories = data
        updateState()
    }

    private fun handleError(error: Throwable) {
        val messageResId = (error as? AppError)?.messageResId ?: R.string.unknown_error
        _screenState.value = Error(
            messageResId = messageResId,
            retryAction = { loadCategories() }
        )
    }


    fun updateState() {
        val query = _searchRequest.value
        val filtered = when {
            query.isBlank() -> cachedCategories
            else -> cachedCategories.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }

        _screenState.value = when {
            filtered.isEmpty() && query.isNotBlank() -> CategoriesScreenState.SearchEmpty
            filtered.isEmpty() -> CategoriesScreenState.Empty
            else -> Success(categories = filtered)
        }
    }

    fun onChangeSearchRequest(request: String) {
        _searchRequest.value = request
        updateState()
    }
}