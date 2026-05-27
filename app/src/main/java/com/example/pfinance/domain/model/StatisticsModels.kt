package com.example.pfinance.domain.model

data class CategorySummary(
    val categoryId: Long,
    val categoryName: String,
    val categoryIcon: String,
    val categoryColor: Int,
    val amount: Double,
    val percentage: Float
)

data class DailySummary(
    val date: String,
    val income: Double,
    val expense: Double
)

data class MonthlySummary(
    val month: String,
    val income: Double,
    val expense: Double,
    val budget: Double = 0.0
)

data class TrendPoint(
    val label: String,
    val amount: Double,
    val type: String // EXPENSE or INCOME
)

data class AssetSnapshot(
    val date: String,
    val totalAssets: Double,
    val totalLiabilities: Double,
    val netWorth: Double
)

data class MerchantSummary(
    val merchantName: String,
    val count: Int,
    val totalAmount: Double
)
