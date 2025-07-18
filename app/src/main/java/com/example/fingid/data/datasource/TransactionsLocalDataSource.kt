package com.example.fingid.data.datasource

import com.example.fingid.data.local.dao.TransactionDao
import com.example.fingid.data.local.model.TransactionEntity
import com.example.fingid.data.local.model.TransactionWithRelations
import javax.inject.Inject

interface TransactionsLocalDataSource {
    suspend fun getAll(): List<TransactionWithRelations>
    suspend fun insertAll(transactions: List<TransactionEntity>)
    suspend fun insert(transaction: TransactionEntity)
    suspend fun delete(id: Int)
    suspend fun getUnsynced(): List<TransactionEntity>
    suspend fun markAsSynced(id: Int)
}

class TransactionsLocalDataSourceImpl @Inject constructor(
    private val transactionDao: TransactionDao
) : TransactionsLocalDataSource {
    override suspend fun getAll(): List<TransactionWithRelations> = transactionDao.getAll()
    override suspend fun insertAll(transactions: List<TransactionEntity>) = transactionDao.insertAll(transactions)
    override suspend fun insert(transaction: TransactionEntity) = transactionDao.insert(transaction)
    override suspend fun delete(id: Int) = transactionDao.delete(id)
    override suspend fun getUnsynced(): List<TransactionEntity> = transactionDao.getUnsynced()
    override suspend fun markAsSynced(id: Int) = transactionDao.markAsSynced(id)
}