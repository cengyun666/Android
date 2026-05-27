package com.example.pfinance.domain.model

import java.time.LocalDateTime

data class Transaction(
    val id: Long = 0,
    val type: TransactionType,
    val amount: Double,
    val currencyCode: String = "CNY",
    val exchangeRate: Double = 1.0,
    val accountId: Long,
    val accountName: String = "",
    val toAccountId: Long? = null,
    val toAccountName: String = "",
    val categoryId: Long? = null,
    val categoryName: String = "",
    val categoryIcon: String = "",
    val categoryColor: Int = 0,
    val date: LocalDateTime,
    val note: String = "",
    val tags: List<String> = emptyList(),
    val isReimbursable: Boolean = false,
    val reimbursementStatus: ReimbursementStatus? = null,
    val refundTransactionId: Long? = null,
    val installmentId: Long? = null,
    val recurringId: Long? = null,
    val fee: Double = 0.0,
    val merchantName: String = "",
    val hasAttachments: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

enum class TransactionType { EXPENSE, INCOME, TRANSFER, REFUND }

enum class ReimbursementStatus { PENDING, APPROVED, PAID }
