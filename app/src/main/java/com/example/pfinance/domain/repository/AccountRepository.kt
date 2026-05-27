package com.example.pfinance.domain.repository

import com.example.pfinance.domain.model.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun getVisibleAccounts(): Flow<List<Account>>
    fun getAllAccounts(): Flow<List<Account>>
    fun getArchivedAccounts(): Flow<List<Account>>
    suspend fun getAccountById(id: Long): Account?
    suspend fun getTotalAssets(): Double
    suspend fun getTotalLiabilities(): Double
    suspend fun insertAccount(account: Account): Long
    suspend fun updateAccount(account: Account)
    suspend fun updateBalance(id: Long, delta: Double)
    suspend fun archiveAccount(id: Long)
    suspend fun unarchiveAccount(id: Long)
    suspend fun deleteAccount(account: Account)
}
