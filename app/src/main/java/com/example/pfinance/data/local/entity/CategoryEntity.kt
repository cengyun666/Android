package com.example.pfinance.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "categories",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["parentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("parentId")]
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: String,              // EXPENSE, INCOME
    val icon: String = "category",
    val color: Int = 0xFF2196F3.toInt(),
    val parentId: Long? = null,    // for sub-categories
    val isSystem: Boolean = false, // system default categories
    val sortOrder: Int = 0,
    val isHidden: Boolean = false
)
