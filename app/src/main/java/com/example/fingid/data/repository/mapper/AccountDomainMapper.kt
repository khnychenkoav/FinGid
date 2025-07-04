package com.example.fingid.data.repository.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.fingid.data.model.AccountBriefDTO
import com.example.fingid.data.model.AccountDTO
import com.example.fingid.data.model.StatItemDTO
import com.example.fingid.domain.model.AccountBriefDomain
import com.example.fingid.domain.model.AccountDomain
import com.example.fingid.domain.model.StatItemDomain
import java.math.RoundingMode
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject


internal class AccountDomainMapper @Inject constructor() {
    @RequiresApi(Build.VERSION_CODES.O)
    fun mapAccount(dto: AccountDTO): AccountDomain {
        val createdAt = Instant.parse(dto.createdAt).atZone(ZoneId.systemDefault())
        val updatedAt = Instant.parse(dto.updatedAt).atZone(ZoneId.systemDefault())

        return AccountDomain(
            id = dto.id,
            name = dto.name,
            balance = dto.balance.toIntFromDecimal(),
            currency = dto.currency,
            incomeStats = dto.incomeStats.map { mapStatItem(it) },
            expenseStats = dto.expenseStats.map { mapStatItem(it) },
            createdAtDate = createdAt.toLocalDate(),
            createdAtTime = createdAt.toLocalTime().truncatedTo(ChronoUnit.MINUTES),
            updatedAtDate = updatedAt.toLocalDate(),
            updatedAtTime = updatedAt.toLocalTime().truncatedTo(ChronoUnit.MINUTES)
        )
    }

    private fun mapStatItem(dto: StatItemDTO): StatItemDomain {
        return StatItemDomain(
            categoryId = dto.categoryId,
            categoryName = dto.categoryName,
            emoji = dto.emoji,
            amount = dto.amount
        )
    }

    fun mapAccountBrief(domain: AccountBriefDomain): AccountBriefDTO {
        return AccountBriefDTO(
            id = domain.id,
            name = domain.name,
            balance = domain.balance.toString(),
            currency = domain.currency
        )
    }
}


private fun String.toIntFromDecimal(): Int {
    return this.toBigDecimal()
        .setScale(0, RoundingMode.DOWN)
        .toInt()
}