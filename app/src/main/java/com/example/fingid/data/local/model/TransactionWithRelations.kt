package com.example.fingid.data.local.model

import androidx.room.Embedded
import androidx.room.Relation


data class TransactionWithRelations (
    @Embedded val transaction: TransactionEntity,

    @Relation(
        parentColumn = "accountId",
        entityColumn = "id"
    )
    val accountModel: AccountEntity,

    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val categoryModel: CategoryEntity
)