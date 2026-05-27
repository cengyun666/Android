package com.example.pfinance.domain.repository

import com.example.pfinance.domain.model.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface StatisticsRepository {
    fun getCategorySummary(type: TransactionType, start: LocalDateTime, end: LocalDateTime): Flow<List<CategorySummary>>
    fun getDailySummary(start: LocalDateTime, end: LocalDateTime): Flow<List<DailySummary>>
    fun getMonthlySummary(year: Int): Flow<List<MonthlySummary>>
    fun getTrend(type: TransactionType, start: LocalDateTime, end: LocalDateTime, groupBy: String): Flow<List<TrendPoint>>
    fun getAssetSnapshots(start: LocalDateTime, end: LocalDateTime): Flow<List<AssetSnapshot>>
    fun getMerchantSummaries(start: LocalDateTime, end: LocalDateTime, limit: Int = 10): Flow<List<MerchantSummary>>
}
