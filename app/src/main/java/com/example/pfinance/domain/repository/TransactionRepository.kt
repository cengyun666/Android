package com.example.pfinance.domain.repository

import com.example.pfinance.domain.model.Transaction
import com.example.pfinance.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface TransactionRepository {
    fun getAllTransactions(): Flow<List<Transaction>>
    suspend fun getTransactionById(id: Long): Transaction?
    fun getTransactionsByDateRange(start: LocalDateTime, end: LocalDateTime): Flow<List<Transaction>>
    fun getTransactionsByType(type: TransactionType): Flow<List<Transaction>>
    fun getTransactionsByAccount(accountId: Long): Flow<List<Transaction>>
    fun getTransactionsByCategory(categoryId: Long): Flow<List<Transaction>>
    fun getReimbursableTransactions(): Flow<List<Transaction>>
    fun getDeletedTransactions(): Flow<List<Transaction>>
    suspend fun getTotalByTypeAndDateRange(type: TransactionType, start: LocalDateTime, end: LocalDateTime): Double
    suspend fun getTotalByCategoryAndDateRange(type: TransactionType, categoryId: Long, start: LocalDateTime, end: LocalDateTime): Double
    suspend fun searchTransactions(query: String): List<Transaction>
    suspend fun insertTransaction(transaction: Transaction): Long
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun softDeleteTransaction(id: Long)
    suspend fun restoreTransaction(id: Long)
    suspend fun permanentlyDeleteOldTransactions()
}
