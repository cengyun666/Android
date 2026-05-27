package com.example.pfinance.domain.model

import java.time.LocalDateTime

data class Budget(
    val id: Long = 0,
    val name: String,
    val type: BudgetType,
    val period: BudgetPeriod,
    val amount: Double,
    val spent: Double = 0.0,
    val categoryId: Long? = null,
    val categoryName: String = "",
    val startDate: LocalDateTime,
    val endDate: LocalDateTime? = null,
    val isActive: Boolean = true,
    val alertThreshold: Double = 0.8
) {
    val progress: Float get() = if (amount > 0) (spent / amount).toFloat().coerceIn(0f, 1f) else 0f
    val remaining: Double get() = amount - spent
    val isOverBudget: Boolean get() = spent > amount
}

enum class BudgetType { TOTAL, CATEGORY }
enum class BudgetPeriod { WEEKLY, MONTHLY, YEARLY }
