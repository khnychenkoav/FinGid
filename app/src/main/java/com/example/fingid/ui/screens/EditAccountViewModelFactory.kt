package com.example.fingid.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EditAccountViewModelFactory(private val accountId: Long?) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditAccountViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditAccountViewModel(accountId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}