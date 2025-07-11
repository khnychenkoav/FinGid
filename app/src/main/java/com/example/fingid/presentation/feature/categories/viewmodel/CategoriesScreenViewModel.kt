package com.example.fingid.presentation.feature.categories.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fingid.R
import com.example.fingid.data.remote.api.AppError
import com.example.fingid.domain.usecases.GetCategoriesUseCase
import com.example.fingid.presentation.feature.categories.mapper.CategoryToCategoryUiMapper
import com.example.fingid.presentation.feature.categories.model.CategoryUiModel
import com.example.fingid.presentation.feature.categories.viewmodel.CategoriesUiState.Content
import com.example.fingid.presentation.feature.categories.viewmodel.CategoriesUiState.Empty
import com.example.fingid.presentation.feature.categories.viewmodel.CategoriesUiState.Error
import com.example.fingid.presentation.feature.categories.viewmodel.CategoriesUiState.Loading
import com.example.fingid.presentation.feature.categories.viewmodel.CategoriesUiState.SearchEmpty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



sealed interface CategoriesUiState {

    data object Loading : CategoriesUiState


    data class Content(val categories: List<CategoryUiModel>) : CategoriesUiState

    data object Empty : CategoriesUiState

    data object SearchEmpty : CategoriesUiState

    data class Error(@StringRes val messageResId: Int) : CategoriesUiState
}


class CategoriesScreenViewModel @Inject constructor(
    private val getCategories: GetCategoriesUseCase,
    private val mapper: CategoryToCategoryUiMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow<CategoriesUiState>(Loading)
    val uiState: StateFlow<CategoriesUiState> = _uiState.asStateFlow()

    private var cachedCategories: List<CategoryUiModel> = emptyList()

    private val _searchRequest = MutableStateFlow("")
    val searchRequest: StateFlow<String> = _searchRequest


    fun init() = viewModelScope.launch(Dispatchers.IO) {
        _uiState.value = Loading

        val result = getCategories()

        result
            .onSuccess { data ->
                cachedCategories = data.map { mapper.map(it) }
                updateState()
            }
            .onFailure { error -> showError(error) }
    }


    fun updateState() {
        val query = _searchRequest.value
        val filtered = when {
            query.isBlank() -> cachedCategories
            else -> cachedCategories.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }

        _uiState.value = when {
            filtered.isEmpty() && query.isNotBlank() -> SearchEmpty
            filtered.isEmpty() -> Empty
            else -> Content(categories = filtered)
        }
    }

    fun onChangeSearchRequest(request: String) {
        _searchRequest.value = request
        updateState()
    }

    private fun showError(t: Throwable) {
        val res = (t as? AppError)?.messageResId ?: R.string.unknown_error
        _uiState.value = Error(messageResId = res)
    }
}