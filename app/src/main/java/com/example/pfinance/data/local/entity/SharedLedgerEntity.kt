package com.example.pfinance.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "shared_ledgers")
data class SharedLedgerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val inviteCode: String,
    val ownerId: String,
    val memberIds: String = "",     // comma-separated user IDs
    val createdAt: LocalDateTime = LocalDateTime.now()
)
