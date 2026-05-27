package com.example.pfinance.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "recurring_transactions")
data class RecurringTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String,              // EXPENSE, INCOME
    val amount: Double,
    val currencyCode: String = "CNY",
    val accountId: Long,
    val categoryId: Long? = null,
    val frequency: String,         // DAILY, WEEKLY, MONTHLY, YEARLY
    val interval: Int = 1,         // every N frequency units
    val nextDate: LocalDateTime,
    val endDate: LocalDateTime? = null,
    val note: String = "",
    val isActive: Boolean = true,
    val autoCreate: Boolean = true, // auto-create transaction on due date
    val remindBefore: Int = 1,      // days before to remind
    val createdAt: LocalDateTime = LocalDateTime.now()
)
