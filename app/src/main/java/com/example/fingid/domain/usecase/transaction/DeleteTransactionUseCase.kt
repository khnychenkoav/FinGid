package com.example.fingid.domain.usecase.transaction

import com.example.fingid.domain.repository.TransactionRepository

class DeleteTransactionUseCase(private val transactionRepository: TransactionRepository) {
    suspend operator fun invoke(id: Int) {
        transactionRepository.deleteTransaction(id)
    }
}
