package com.example.pfinance.data.repository

import com.example.pfinance.data.local.dao.BudgetDao
import com.example.pfinance.data.local.dao.TransactionDao
import com.example.pfinance.data.local.entity.BudgetEntity
import com.example.pfinance.domain.model.*
import com.example.pfinance.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao,
    private val transactionDao: TransactionDao
) : BudgetRepository {

    override fun getActiveBudgets(): Flow<List<Budget>> =
        budgetDao.getActiveBudgets().map { list -> list.map { it.toDomain() } }

    override fun getAllBudgets(): Flow<List<Budget>> =
        budgetDao.getAllBudgets().map { list -> list.map { it.toDomain() } }

    override suspend fun getBudgetById(id: Long): Budget? =
        budgetDao.getBudgetById(id)?.toDomain()

    override fun getActiveBudgetsByType(type: BudgetType): Flow<List<Budget>> =
        budgetDao.getActiveBudgetsByType(type.name).map { list -> list.map { it.toDomain() } }

    override suspend fun insertBudget(budget: Budget): Long =
        budgetDao.insertBudget(budget.toEntity())

    override suspend fun updateBudget(budget: Budget) =
        budgetDao.updateBudget(budget.toEntity())

    override suspend fun deleteBudget(budget: Budget) =
        budgetDao.deleteBudget(budget.toEntity())

    private suspend fun BudgetEntity.toDomain(): Budget {
        val now = LocalDateTime.now()
        val startDate = this.startDate
        val endDate = this.endDate ?: when (this.period) {
            "WEEKLY" -> startDate.plusWeeks(1)
            "MONTHLY" -> startDate.plusMonths(1)
            "YEARLY" -> startDate.plusYears(1)
            else -> startDate.plusMonths(1)
        }
        val spent = if (this.type == "CATEGORY" && this.categoryId != null) {
            transactionDao.getTotalByCategoryAndDateRange("EXPENSE", this.categoryId, startDate, endDate) ?: 0.0
        } else {
            transactionDao.getTotalByTypeAndDateRange("EXPENSE", startDate, endDate) ?: 0.0
        }
        return Budget(
            id = this.id, name = this.name,
            type = BudgetType.valueOf(this.type),
            period = BudgetPeriod.valueOf(this.period),
            amount = this.amount, spent = spent,
            categoryId = this.categoryId,
            startDate = this.startDate, endDate = this.endDate,
            isActive = this.isActive, alertThreshold = this.alertThreshold
        )
    }

    private fun Budget.toEntity() = BudgetEntity(
        id = this.id, name = this.name, type = this.type.name,
        period = this.period.name, amount = this.amount, categoryId = this.categoryId,
        startDate = this.startDate, endDate = this.endDate,
        isActive = this.isActive, alertThreshold = this.alertThreshold
    )
}
