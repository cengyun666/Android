package com.example.pfinance.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pfinance.data.local.converter.Converters
import com.example.pfinance.data.local.dao.*
import com.example.pfinance.data.local.entity.*

@Database(
    entities = [
        TransactionEntity::class,
        AccountEntity::class,
        CategoryEntity::class,
        BudgetEntity::class,
        RecurringTransactionEntity::class,
        InstallmentEntity::class,
        SavingGoalEntity::class,
        TagEntity::class,
        AttachmentEntity::class,
        SharedLedgerEntity::class,
        ExchangeRateEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun budgetDao(): BudgetDao
    abstract fun recurringTransactionDao(): RecurringTransactionDao
    abstract fun installmentDao(): InstallmentDao
    abstract fun savingGoalDao(): SavingGoalDao
    abstract fun exchangeRateDao(): ExchangeRateDao
    abstract fun attachmentDao(): AttachmentDao
}
