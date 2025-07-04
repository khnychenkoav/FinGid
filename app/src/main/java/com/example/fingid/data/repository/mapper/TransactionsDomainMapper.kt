package com.example.fingid.data.repository.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.fingid.data.model.AccountBriefDTO
import com.example.fingid.data.model.TransactionDTO
import com.example.fingid.domain.model.AccountBriefDomain
import com.example.fingid.domain.model.TransactionDomain
import java.math.RoundingMode
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject


internal class TransactionsDomainMapper @Inject constructor(
    private val categoryMapper: CategoryDomainMapper
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun mapTransaction(dto: TransactionDTO): TransactionDomain {
        val transactionAt = Instant.parse(dto.transactionDate).atZone(ZoneId.systemDefault())

        return TransactionDomain(
            id = dto.id,
            account = mapAccountBrief(dto.account),
            category = categoryMapper.mapCategory(dto.category),
            amount = dto.amount.toIntFromDecimal(),
            transactionDate = transactionAt.toLocalDate(),
            transactionTime = transactionAt.toLocalTime().truncatedTo(ChronoUnit.MINUTES),
            comment = dto.comment,
        )
    }

    private fun mapAccountBrief(dto: AccountBriefDTO): AccountBriefDomain {
        return AccountBriefDomain(
            id = dto.id,
            name = dto.name,
            balance = dto.balance.toIntFromDecimal(),
            currency = dto.currency
        )
    }
}


private fun String.toIntFromDecimal(): Int {
    return this.toBigDecimal()
        .setScale(0, RoundingMode.DOWN)
        .toInt()
}