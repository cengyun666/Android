package com.example.pfinance.domain.model

data class Category(
    val id: Long = 0,
    val name: String,
    val type: CategoryType,
    val icon: String = "category",
    val color: Int = 0xFF2196F3.toInt(),
    val parentId: Long? = null,
    val isSystem: Boolean = false
)

enum class CategoryType { EXPENSE, INCOME }
