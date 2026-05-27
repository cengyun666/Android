package com.example.pfinance.data.repository

import com.example.pfinance.data.local.dao.AccountDao
import com.example.pfinance.data.local.dao.CategoryDao
import com.example.pfinance.data.local.dao.TransactionDao
import com.example.pfinance.domain.model.*
import com.example.pfinance.domain.repository.StatisticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatisticsRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao,
    private val accountDao: AccountDao
) : StatisticsRepository {

    override fun getCategorySummary(type: TransactionType, start: LocalDateTime, end: LocalDateTime): Flow<List<CategorySummary>> {
        return transactionDao.getTransactionsByTypeAndDateRange(type.name, start, end).map { transactions ->
            val categories = categoryDao.getCategoriesByType(type.name).first()
            val totalAmount = transactions.sumOf { it.amount }
            categories.map { cat ->
                val amount = transactions.filter { it.categoryId == cat.id }.sumOf { it.amount }
                CategorySummary(
                    categoryId = cat.id,
                    categoryName = cat.name,
                    categoryIcon = cat.icon,
                    categoryColor = cat.color,
                    amount = amount,
                    percentage = if (totalAmount > 0) (amount / totalAmount).toFloat() else 0f
                )
            }.sortedByDescending { it.amount }
        }
    }

    override fun getDailySummary(start: LocalDateTime, end: LocalDateTime): Flow<List<DailySummary>> {
        return transactionDao.getTransactionsByDateRange(start, end).map { transactions ->
            transactions.groupBy { it.date.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE) }
                .map { (date, list) ->
                    DailySummary(
                        date = date,
                        income = list.filter { it.type == "INCOME" }.sumOf { it.amount },
                        expense = list.filter { it.type == "EXPENSE" }.sumOf { it.amount }
                    )
                }.sortedBy { it.date }
        }
    }

    override fun getMonthlySummary(year: Int): Flow<List<MonthlySummary>> {
        val start = LocalDateTime.of(year, 1, 1, 0, 0)
        val end = LocalDateTime.of(year, 12, 31, 23, 59)
        return transactionDao.getTransactionsByDateRange(start, end).map { transactions ->
            (1..12).map { month ->
                val monthTransactions = transactions.filter { it.date.monthValue == month }
                MonthlySummary(
                    month = "$month月",
                    income = monthTransactions.filter { it.type == "INCOME" }.sumOf { it.amount },
                    expense = monthTransactions.filter { it.type == "EXPENSE" }.sumOf { it.amount }
                )
            }
        }
    }

    override fun getTrend(type: TransactionType, start: LocalDateTime, end: LocalDateTime, groupBy: String): Flow<List<TrendPoint>> {
        return transactionDao.getTransactionsByTypeAndDateRange(type.name, start, end).map { transactions ->
            transactions.groupBy { tx ->
                when (groupBy) {
                    "DAY" -> tx.date.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE)
                    "WEEK" -> {
                        val date = tx.date.toLocalDate()
                        "W${date.get(java.time.temporal.ChronoField.ALIGNED_WEEK_OF_YEAR)}"
                    }
                    "MONTH" -> "${tx.date.year}-${tx.date.monthValue}"
                    "YEAR" -> "${tx.date.year}"
                    else -> tx.date.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE)
                }
            }.map { (label, list) ->
                TrendPoint(label = label, amount = list.sumOf { it.amount }, type = type.name)
            }.sortedBy { it.label }
        }
    }

    override fun getAssetSnapshots(start: LocalDateTime, end: LocalDateTime): Flow<List<AssetSnapshot>> = TODO()

    override fun getMerchantSummaries(start: LocalDateTime, end: LocalDateTime, limit: Int): Flow<List<MerchantSummary>> {
        return transactionDao.getTransactionsByTypeAndDateRange("EXPENSE", start, end).map { transactions ->
            transactions.filter { it.merchantName.isNotBlank() }
                .groupBy { it.merchantName }
                .map { (name, list) ->
                    MerchantSummary(
                        merchantName = name,
                        count = list.size,
                        totalAmount = list.sumOf { it.amount }
                    )
                }
                .sortedByDescending { it.totalAmount }
                .take(limit)
        }
    }
}
