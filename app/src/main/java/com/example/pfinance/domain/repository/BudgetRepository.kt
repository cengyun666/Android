package com.example.pfinance.domain.repository

import com.example.pfinance.domain.model.Budget
import com.example.pfinance.domain.model.BudgetType
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {
    fun getActiveBudgets(): Flow<List<Budget>>
    fun getAllBudgets(): Flow<List<Budget>>
    suspend fun getBudgetById(id: Long): Budget?
    fun getActiveBudgetsByType(type: BudgetType): Flow<List<Budget>>
    suspend fun insertBudget(budget: Budget): Long
    suspend fun updateBudget(budget: Budget)
    suspend fun deleteBudget(budget: Budget)
}
