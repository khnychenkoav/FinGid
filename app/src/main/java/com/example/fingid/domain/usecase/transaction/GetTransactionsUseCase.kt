package com.example.fingid.domain.usecase.transaction

import com.example.fingid.domain.repository.TransactionRepository

class GetTransactionsUseCase(val transactionRepository: TransactionRepository) {
    suspend operator fun invoke(accountId: Int, from: String, to: String) =
        transactionRepository.getTransactions(accountId, from, to)
}
