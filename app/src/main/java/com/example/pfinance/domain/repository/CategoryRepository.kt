package com.example.pfinance.domain.repository

import com.example.pfinance.domain.model.Category
import com.example.pfinance.domain.model.CategoryType
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getVisibleCategories(): Flow<List<Category>>
    fun getCategoriesByType(type: CategoryType): Flow<List<Category>>
    fun getSubCategories(parentId: Long): Flow<List<Category>>
    fun getParentCategories(): Flow<List<Category>>
    suspend fun getCategoryById(id: Long): Category?
    suspend fun insertCategory(category: Category): Long
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)
    suspend fun initDefaultCategories()
}
