package com.example.fingid.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fingid.data.local.dao.AccountDao
import com.example.fingid.data.local.dao.CategoryDao
import com.example.fingid.data.local.dao.TransactionDao
import com.example.fingid.data.local.model.AccountEntity
import com.example.fingid.data.local.model.CategoryEntity
import com.example.fingid.data.local.model.TransactionEntity

@Database(
    entities = [
        CategoryEntity::class,
        AccountEntity::class,
        TransactionEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

    abstract fun accountDao(): AccountDao

    abstract fun transactionDao(): TransactionDao
}