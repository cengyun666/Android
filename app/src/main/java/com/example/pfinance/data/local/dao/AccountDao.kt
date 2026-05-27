package com.example.pfinance.data.local.dao

import androidx.room.*
import com.example.pfinance.data.local.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Query("SELECT * FROM accounts WHERE isArchived = 0 ORDER BY sortOrder ASC")
    fun getVisibleAccounts(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM accounts ORDER BY sortOrder ASC")
    fun getAllAccounts(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM accounts WHERE isArchived = 1 ORDER BY sortOrder ASC")
    fun getArchivedAccounts(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM accounts WHERE id = :id")
    suspend fun getAccountById(id: Long): AccountEntity?

    @Query("SELECT SUM(balance) FROM accounts WHERE isHidden = 0 AND isArchived = 0 AND type != 'CREDIT_CARD'")
    suspend fun getTotalAssets(): Double?

    @Query("SELECT SUM(balance) FROM accounts WHERE isHidden = 0 AND isArchived = 0 AND type = 'CREDIT_CARD'")
    suspend fun getTotalLiabilities(): Double?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: AccountEntity): Long

    @Update
    suspend fun updateAccount(account: AccountEntity)

    @Query("UPDATE accounts SET balance = balance + :delta WHERE id = :id")
    suspend fun updateBalance(id: Long, delta: Double)

    @Query("UPDATE accounts SET isArchived = 1 WHERE id = :id")
    suspend fun archiveAccount(id: Long)

    @Query("UPDATE accounts SET isArchived = 0 WHERE id = :id")
    suspend fun unarchiveAccount(id: Long)

    @Delete
    suspend fun deleteAccount(account: AccountEntity)
}
