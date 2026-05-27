package com.example.pfinance.data.local.dao

import androidx.room.*
import com.example.pfinance.data.local.entity.InstallmentEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface InstallmentDao {
    @Query("SELECT * FROM installments WHERE isActive = 1 ORDER BY nextDate ASC")
    fun getActiveInstallments(): Flow<List<InstallmentEntity>>

    @Query("SELECT * FROM installments ORDER BY nextDate ASC")
    fun getAllInstallments(): Flow<List<InstallmentEntity>>

    @Query("SELECT * FROM installments WHERE id = :id")
    suspend fun getById(id: Long): InstallmentEntity?

    @Query("SELECT * FROM installments WHERE nextDate <= :date AND isActive = 1")
    suspend fun getDueInstallments(date: LocalDateTime): List<InstallmentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(installment: InstallmentEntity): Long

    @Update
    suspend fun update(installment: InstallmentEntity)

    @Delete
    suspend fun delete(installment: InstallmentEntity)
}
