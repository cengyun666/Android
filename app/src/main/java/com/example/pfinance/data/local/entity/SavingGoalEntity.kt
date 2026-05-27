package com.example.pfinance.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "saving_goals")
data class SavingGoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val targetAmount: Double,
    val currentAmount: Double = 0.0,
    val icon: String = "savings",
    val color: Int = 0xFFFF9800.toInt(),
    val deadline: LocalDateTime? = null,
    val note: String = "",
    val isCompleted: Boolean = false,
    val sortOrder: Int = 0,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
