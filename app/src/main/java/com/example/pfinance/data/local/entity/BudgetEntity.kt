package com.example.pfinance.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: String,              // TOTAL, CATEGORY
    val period: String,            // WEEKLY, MONTHLY, YEARLY
    val amount: Double,
    val categoryId: Long? = null,  // null for total budget
    val startDate: LocalDateTime,
    val endDate: LocalDateTime? = null,
    val isActive: Boolean = true,
    val alertThreshold: Double = 0.8, // 0.0 - 1.0
    val createdAt: LocalDateTime = LocalDateTime.now()
)
