package com.example.fingid.data.remote.mapper

import com.example.fingid.data.model.AccountBriefDTO
import com.example.fingid.data.model.AccountDTO
import com.example.fingid.data.model.StatItemDTO
import com.example.fingid.data.remote.model.AccountResponse
import com.example.fingid.data.remote.model.AccountUpdateRequest
import com.example.fingid.data.remote.model.StatItemResponse
import dagger.Reusable
import javax.inject.Inject


@Reusable
internal class AccountRemoteMapper @Inject constructor() {
    fun mapAccount(response: AccountResponse): AccountDTO {
        return AccountDTO(
            id = response.id,
            name = response.name,
            balance = response.balance,
            currency = response.currency,
            incomeStats = response.incomeStats.map { mapStatItem(it) },
            expenseStats = response.expenseStats.map { mapStatItem(it) },
            createdAt = response.createdAt,
            updatedAt = response.updatedAt
        )
    }

    private fun mapStatItem(item: StatItemResponse): StatItemDTO {
        return StatItemDTO(
            categoryId = item.categoryId,
            categoryName = item.categoryName,
            emoji = item.emoji,
            amount = item.amount
        )
    }

    fun mapAccountBrief(brief: AccountBriefDTO): AccountUpdateRequest {
        return AccountUpdateRequest(
            name = brief.name,
            balance = brief.balance,
            currency = brief.currency
        )
    }
}