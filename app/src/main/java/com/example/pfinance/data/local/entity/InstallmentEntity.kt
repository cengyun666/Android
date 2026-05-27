package com.example.pfinance.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "installments")
data class InstallmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val transactionId: Long,       // original purchase transaction
    val totalAmount: Double,
    val totalPeriods: Int,
    val currentPeriod: Int = 0,
    val amountPerPeriod: Double,
    val frequency: String = "MONTHLY", // MONTHLY, BI_WEEKLY
    val startDate: LocalDateTime,
    val nextDate: LocalDateTime,
    val accountId: Long,
    val categoryId: Long? = null,
    val note: String = "",
    val isActive: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
