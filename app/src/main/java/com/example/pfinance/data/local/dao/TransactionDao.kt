package com.example.pfinance.data.local.dao

import androidx.room.*
import com.example.pfinance.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE isDeleted = 0 ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id AND isDeleted = 0")
    suspend fun getTransactionById(id: Long): TransactionEntity?

    @Query("""
        SELECT * FROM transactions
        WHERE isDeleted = 0 AND date BETWEEN :start AND :end
        ORDER BY date DESC
    """)
    fun getTransactionsByDateRange(start: LocalDateTime, end: LocalDateTime): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * FROM transactions
        WHERE isDeleted = 0 AND type = :type
        ORDER BY date DESC
    """)
    fun getTransactionsByType(type: String): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * FROM transactions
        WHERE isDeleted = 0 AND accountId = :accountId
        ORDER BY date DESC
    """)
    fun getTransactionsByAccount(accountId: Long): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * FROM transactions
        WHERE isDeleted = 0 AND categoryId = :categoryId
        ORDER BY date DESC
    """)
    fun getTransactionsByCategory(categoryId: Long): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * FROM transactions
        WHERE isDeleted = 0 AND type = :type AND date BETWEEN :start AND :end
        ORDER BY date DESC
    """)
    fun getTransactionsByTypeAndDateRange(
        type: String, start: LocalDateTime, end: LocalDateTime
    ): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * FROM transactions
        WHERE isDeleted = 0 AND isReimbursable = 1
        ORDER BY date DESC
    """)
    fun getReimbursableTransactions(): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * FROM transactions
        WHERE isDeleted = 1 AND deletedAt >= :recycleDate
        ORDER BY deletedAt DESC
    """)
    fun getDeletedTransactions(recycleDate: LocalDateTime): Flow<List<TransactionEntity>>

    @Query("SELECT SUM(amount) FROM transactions WHERE isDeleted = 0 AND type = :type AND date BETWEEN :start AND :end")
    suspend fun getTotalByTypeAndDateRange(type: String, start: LocalDateTime, end: LocalDateTime): Double?

    @Query("""
        SELECT SUM(amount) FROM transactions
        WHERE isDeleted = 0 AND type = :type AND categoryId = :categoryId
        AND date BETWEEN :start AND :end
    """)
    suspend fun getTotalByCategoryAndDateRange(
        type: String, categoryId: Long, start: LocalDateTime, end: LocalDateTime
    ): Double?

    @Query("SELECT SUM(amount) FROM transactions WHERE isDeleted = 0 AND type = :type AND accountId = :accountId")
    suspend fun getTotalByTypeAndAccount(type: String, accountId: Long): Double?

    @Query("""
        SELECT * FROM transactions
        WHERE isDeleted = 0 AND (note LIKE :query OR merchantName LIKE :query)
        ORDER BY date DESC
    """)
    suspend fun searchTransactions(query: String): List<TransactionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Query("UPDATE transactions SET isDeleted = 1, deletedAt = :deletedAt WHERE id = :id")
    suspend fun softDeleteTransaction(id: Long, deletedAt: LocalDateTime = LocalDateTime.now())

    @Query("UPDATE transactions SET isDeleted = 0, deletedAt = NULL WHERE id = :id")
    suspend fun restoreTransaction(id: Long)

    @Query("DELETE FROM transactions WHERE isDeleted = 1 AND deletedAt < :before")
    suspend fun permanentlyDeleteOldTransactions(before: LocalDateTime)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)
}
