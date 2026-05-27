package com.example.pfinance.data.local.dao

import androidx.room.*
import com.example.pfinance.data.local.entity.SavingGoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavingGoalDao {
    @Query("SELECT * FROM saving_goals ORDER BY sortOrder ASC")
    fun getAllGoals(): Flow<List<SavingGoalEntity>>

    @Query("SELECT * FROM saving_goals WHERE isCompleted = 0 ORDER BY sortOrder ASC")
    fun getActiveGoals(): Flow<List<SavingGoalEntity>>

    @Query("SELECT * FROM saving_goals WHERE id = :id")
    suspend fun getGoalById(id: Long): SavingGoalEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: SavingGoalEntity): Long

    @Update
    suspend fun updateGoal(goal: SavingGoalEntity)

    @Delete
    suspend fun deleteGoal(goal: SavingGoalEntity)
}
