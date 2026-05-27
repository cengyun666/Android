package com.example.pfinance.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: String,              // CASH, SAVINGS, CREDIT_CARD, ALIPAY, WECHAT, INVESTMENT, STORED_VALUE, CUSTOM
    val balance: Double = 0.0,
    val initialBalance: Double = 0.0,
    val currencyCode: String = "CNY",
    val icon: String = "account_balance_wallet",
    val color: Int = 0xFF4CAF50.toInt(),
    // Credit card fields
    val creditLimit: Double? = null,
    val billDate: Int? = null,     // day of month
    val dueDate: Int? = null,      // day of month
    val cardNumber: String = "",   // last 4 digits only
    // General
    val note: String = "",
    val isHidden: Boolean = false,
    val isArchived: Boolean = false,
    val sortOrder: Int = 0,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
