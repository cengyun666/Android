package com.example.pfinance.data.repository

import com.example.pfinance.data.local.dao.AccountDao
import com.example.pfinance.data.local.dao.CategoryDao
import com.example.pfinance.data.local.dao.TransactionDao
import com.example.pfinance.data.local.entity.TransactionEntity
import com.example.pfinance.domain.model.*
import com.example.pfinance.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao,
    private val categoryDao: CategoryDao
) : TransactionRepository {

    override fun getAllTransactions(): Flow<List<Transaction>> =
        transactionDao.getAllTransactions().map { list -> list.map { it.toDomain() } }

    override suspend fun getTransactionById(id: Long): Transaction? =
        transactionDao.getTransactionById(id)?.toDomain()

    override fun getTransactionsByDateRange(start: LocalDateTime, end: LocalDateTime): Flow<List<Transaction>> =
        transactionDao.getTransactionsByDateRange(start, end).map { list -> list.map { it.toDomain() } }

    override fun getTransactionsByType(type: TransactionType): Flow<List<Transaction>> =
        transactionDao.getTransactionsByType(type.name).map { list -> list.map { it.toDomain() } }

    override fun getTransactionsByAccount(accountId: Long): Flow<List<Transaction>> =
        transactionDao.getTransactionsByAccount(accountId).map { list -> list.map { it.toDomain() } }

    override fun getTransactionsByCategory(categoryId: Long): Flow<List<Transaction>> =
        transactionDao.getTransactionsByCategory(categoryId).map { list -> list.map { it.toDomain() } }

    override fun getReimbursableTransactions(): Flow<List<Transaction>> =
        transactionDao.getReimbursableTransactions().map { list -> list.map { it.toDomain() } }

    override fun getDeletedTransactions(): Flow<List<Transaction>> =
        transactionDao.getDeletedTransactions(LocalDateTime.now().minusDays(30))
            .map { list -> list.map { it.toDomain() } }

    override suspend fun getTotalByTypeAndDateRange(type: TransactionType, start: LocalDateTime, end: LocalDateTime): Double =
        transactionDao.getTotalByTypeAndDateRange(type.name, start, end) ?: 0.0

    override suspend fun getTotalByCategoryAndDateRange(type: TransactionType, categoryId: Long, start: LocalDateTime, end: LocalDateTime): Double =
        transactionDao.getTotalByCategoryAndDateRange(type.name, categoryId, start, end) ?: 0.0

    override suspend fun searchTransactions(query: String): List<Transaction> =
        transactionDao.searchTransactions(query).map { it.toDomain() }

    override suspend fun insertTransaction(transaction: Transaction): Long {
        return transactionDao.insertTransaction(transaction.toEntity())
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction.toEntity())
    }

    override suspend fun softDeleteTransaction(id: Long) {
        transactionDao.softDeleteTransaction(id)
    }

    override suspend fun restoreTransaction(id: Long) {
        transactionDao.restoreTransaction(id)
    }

    override suspend fun permanentlyDeleteOldTransactions() {
        transactionDao.permanentlyDeleteOldTransactions(LocalDateTime.now().minusDays(30))
    }

    private suspend fun TransactionEntity.toDomain(): Transaction {
        val account = accountDao.getAccountById(this.accountId)
        val toAccount = this.toAccountId?.let { accountDao.getAccountById(it) }
        val category = this.categoryId?.let { categoryDao.getCategoryById(it) }
        return Transaction(
            id = this.id,
            type = TransactionType.valueOf(this.type),
            amount = this.amount,
            currencyCode = this.currencyCode,
            exchangeRate = this.exchangeRate,
            accountId = this.accountId,
            accountName = account?.name ?: "",
            toAccountId = this.toAccountId,
            toAccountName = toAccount?.name ?: "",
            categoryId = this.categoryId,
            categoryName = category?.name ?: "",
            categoryIcon = category?.icon ?: "",
            categoryColor = category?.color ?: 0,
            date = this.date,
            note = this.note,
            tags = this.tags.split(",").filter { it.isNotBlank() },
            isReimbursable = this.isReimbursable,
            reimbursementStatus = this.reimbursementStatus?.let { ReimbursementStatus.valueOf(it) },
            refundTransactionId = this.refundTransactionId,
            installmentId = this.installmentId,
            recurringId = this.recurringId,
            fee = this.fee,
            merchantName = this.merchantName,
            createdAt = this.createdAt
        )
    }

    private fun Transaction.toEntity() = TransactionEntity(
        id = this.id,
        type = this.type.name,
        amount = this.amount,
        currencyCode = this.currencyCode,
        exchangeRate = this.exchangeRate,
        accountId = this.accountId,
        toAccountId = this.toAccountId,
        categoryId = this.categoryId,
        date = this.date,
        note = this.note,
        tags = this.tags.joinToString(","),
        isReimbursable = this.isReimbursable,
        reimbursementStatus = this.reimbursementStatus?.name,
        refundTransactionId = this.refundTransactionId,
        installmentId = this.installmentId,
        recurringId = this.recurringId,
        fee = this.fee,
        merchantName = this.merchantName
    )
}
