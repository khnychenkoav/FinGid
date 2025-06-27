package com.example.fingid.domain.usecase.transaction

import com.example.fingid.domain.model.Transaction
import com.example.fingid.domain.repository.TransactionRepository

class CreateTransactionUseCase(val transactionRepository: TransactionRepository) {
    suspend operator fun invoke(transaction: Transaction) =
        transactionRepository.createTransaction(transaction)
}
