package com.example.pfinance.domain.model

import java.time.LocalDateTime

data class RecurringTransaction(
    val id: Long = 0,
    val type: TransactionType,
    val amount: Double,
    val currencyCode: String = "CNY",
    val accountId: Long,
    val accountName: String = "",
    val categoryId: Long? = null,
    val categoryName: String = "",
    val frequency: RecurringFrequency,
    val interval: Int = 1,
    val nextDate: LocalDateTime,
    val endDate: LocalDateTime? = null,
    val note: String = "",
    val isActive: Boolean = true,
    val autoCreate: Boolean = true,
    val remindBefore: Int = 1
)

enum class RecurringFrequency { DAILY, WEEKLY, MONTHLY, YEARLY }
