package com.example.pfinance.di

import android.content.Context
import androidx.room.Room
import com.example.pfinance.data.local.AppDatabase
import com.example.pfinance.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context, AppDatabase::class.java, "pfinance.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides fun provideTransactionDao(db: AppDatabase): TransactionDao = db.transactionDao()
    @Provides fun provideAccountDao(db: AppDatabase): AccountDao = db.accountDao()
    @Provides fun provideCategoryDao(db: AppDatabase): CategoryDao = db.categoryDao()
    @Provides fun provideBudgetDao(db: AppDatabase): BudgetDao = db.budgetDao()
    @Provides fun provideRecurringTransactionDao(db: AppDatabase): RecurringTransactionDao = db.recurringTransactionDao()
    @Provides fun provideInstallmentDao(db: AppDatabase): InstallmentDao = db.installmentDao()
    @Provides fun provideSavingGoalDao(db: AppDatabase): SavingGoalDao = db.savingGoalDao()
    @Provides fun provideExchangeRateDao(db: AppDatabase): ExchangeRateDao = db.exchangeRateDao()
    @Provides fun provideAttachmentDao(db: AppDatabase): AttachmentDao = db.attachmentDao()
}
