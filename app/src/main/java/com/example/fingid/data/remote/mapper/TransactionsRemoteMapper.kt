package com.example.fingid.data.remote.mapper

import com.example.fingid.data.model.AccountBriefDTO
import com.example.fingid.data.model.TransactionDTO
import com.example.fingid.data.model.TransactionResponseDTO
import com.example.fingid.data.remote.model.AccountBrief
import com.example.fingid.data.remote.model.Transaction
import com.example.fingid.data.remote.model.TransactionRequest
import com.example.fingid.data.remote.model.TransactionResponse
import dagger.Reusable
import javax.inject.Inject


@Reusable
internal class TransactionsRemoteMapper @Inject constructor(
    private val categoryMapper: CategoryRemoteMapper
) {
    fun mapTransactionResponse(response: TransactionResponse): TransactionResponseDTO {
        return TransactionResponseDTO(
            id = response.id,
            account = mapAccountBrief(response.account),
            category = categoryMapper.mapCategory(response.category),
            amount = response.amount,
            transactionDate = response.transactionDate,
            comment = response.comment,
            createdAt = response.createdAt,
            updatedAt = response.updatedAt
        )
    }

    private fun mapAccountBrief(brief: AccountBrief): AccountBriefDTO {
        return AccountBriefDTO(
            id = brief.id,
            name = brief.name,
            balance = brief.balance,
            currency = brief.currency
        )
    }

    fun mapTransaction(transaction: Transaction): TransactionDTO {
        return TransactionDTO(
            id = transaction.id,
            accountId = transaction.id,
            categoryId = transaction.id,
            amount = transaction.amount,
            transactionDate = transaction.transactionDate,
            comment = transaction.comment,
            createdAt = transaction.createdAt,
            updatedAt = transaction.updatedAt
        )
    }

    fun mapTransactionToRequest(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): TransactionRequest {
        return TransactionRequest(
            accountId = accountId,
            categoryId = categoryId,
            amount = amount,
            transactionDate = transactionDate,
            comment = comment
        )
    }
}