package com.example.pfinance.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index
import java.time.LocalDateTime

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["toAccountId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("accountId"),
        Index("categoryId"),
        Index("toAccountId"),
        Index("date")
    ]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String,              // EXPENSE, INCOME, TRANSFER, REFUND
    val amount: Double,
    val currencyCode: String = "CNY",
    val exchangeRate: Double = 1.0,
    val accountId: Long,
    val toAccountId: Long? = null,  // for transfers
    val categoryId: Long? = null,
    val date: LocalDateTime,
    val note: String = "",
    val tags: String = "",          // comma-separated tag names
    val isReimbursable: Boolean = false,
    val reimbursementStatus: String? = null, // PENDING, APPROVED, PAID
    val refundTransactionId: Long? = null,   // original transaction for refunds
    val installmentId: Long? = null,         // linked installment plan
    val recurringId: Long? = null,           // linked recurring bill
    val fee: Double = 0.0,                  // transfer fee
    val latitude: Double? = null,
    val longitude: Double? = null,
    val merchantName: String = "",
    val isDeleted: Boolean = false,
    val deletedAt: LocalDateTime? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
