package com.example.pfinance.domain.model

import java.time.LocalDateTime

data class SavingGoal(
    val id: Long = 0,
    val name: String,
    val targetAmount: Double,
    val currentAmount: Double = 0.0,
    val icon: String = "savings",
    val color: Int = 0xFFFF9800.toInt(),
    val deadline: LocalDateTime? = null,
    val note: String = "",
    val isCompleted: Boolean = false
) {
    val progress: Float get() = if (targetAmount > 0) (currentAmount / targetAmount).toFloat().coerceIn(0f, 1f) else 0f
}
