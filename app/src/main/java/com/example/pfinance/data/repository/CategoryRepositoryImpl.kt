package com.example.pfinance.data.repository

import com.example.pfinance.data.local.dao.CategoryDao
import com.example.pfinance.data.local.entity.CategoryEntity
import com.example.pfinance.domain.model.Category
import com.example.pfinance.domain.model.CategoryType
import com.example.pfinance.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override fun getVisibleCategories(): Flow<List<Category>> =
        categoryDao.getVisibleCategories().map { list -> list.map { it.toDomain() } }

    override fun getCategoriesByType(type: CategoryType): Flow<List<Category>> =
        categoryDao.getCategoriesByType(type.name).map { list -> list.map { it.toDomain() } }

    override fun getSubCategories(parentId: Long): Flow<List<Category>> =
        categoryDao.getSubCategories(parentId).map { list -> list.map { it.toDomain() } }

    override fun getParentCategories(): Flow<List<Category>> =
        categoryDao.getParentCategories().map { list -> list.map { it.toDomain() } }

    override suspend fun getCategoryById(id: Long): Category? =
        categoryDao.getCategoryById(id)?.toDomain()

    override suspend fun insertCategory(category: Category): Long =
        categoryDao.insertCategory(category.toEntity())

    override suspend fun updateCategory(category: Category) =
        categoryDao.updateCategory(category.toEntity())

    override suspend fun deleteCategory(category: Category) =
        categoryDao.deleteCategory(category.toEntity())

    override suspend fun initDefaultCategories() {
        val existingCategories = categoryDao.getVisibleCategories().first()
        if (existingCategories.isNotEmpty()) return

        val defaultExpenseCategories = listOf(
            CategoryEntity(name = "餐饮", type = "EXPENSE", icon = "restaurant", color = 0xFFFF5722.toInt(), isSystem = true, sortOrder = 0),
            CategoryEntity(name = "交通", type = "EXPENSE", icon = "directions_car", color = 0xFF2196F3.toInt(), isSystem = true, sortOrder = 1),
            CategoryEntity(name = "购物", type = "EXPENSE", icon = "shopping_cart", color = 0xFFE91E63.toInt(), isSystem = true, sortOrder = 2),
            CategoryEntity(name = "居住", type = "EXPENSE", icon = "home", color = 0xFF795548.toInt(), isSystem = true, sortOrder = 3),
            CategoryEntity(name = "娱乐", type = "EXPENSE", icon = "sports_esports", color = 0xFF9C27B0.toInt(), isSystem = true, sortOrder = 4),
            CategoryEntity(name = "医疗", type = "EXPENSE", icon = "local_hospital", color = 0xFFF44336.toInt(), isSystem = true, sortOrder = 5),
            CategoryEntity(name = "教育", type = "EXPENSE", icon = "school", color = 0xFF3F51B5.toInt(), isSystem = true, sortOrder = 6),
            CategoryEntity(name = "通讯", type = "EXPENSE", icon = "phone", color = 0xFF00BCD4.toInt(), isSystem = true, sortOrder = 7),
            CategoryEntity(name = "服饰", type = "EXPENSE", icon = "checkroom", color = 0xFFFF9800.toInt(), isSystem = true, sortOrder = 8),
            CategoryEntity(name = "其他", type = "EXPENSE", icon = "more_horiz", color = 0xFF607D8B.toInt(), isSystem = true, sortOrder = 9)
        )
        val defaultIncomeCategories = listOf(
            CategoryEntity(name = "工资", type = "INCOME", icon = "work", color = 0xFF4CAF50.toInt(), isSystem = true, sortOrder = 0),
            CategoryEntity(name = "奖金", type = "INCOME", icon = "emoji_events", color = 0xFFFFC107.toInt(), isSystem = true, sortOrder = 1),
            CategoryEntity(name = "投资收益", type = "INCOME", icon = "trending_up", color = 0xFF009688.toInt(), isSystem = true, sortOrder = 2),
            CategoryEntity(name = "兼职", type = "INCOME", icon = "handyman", color = 0xFF8BC34A.toInt(), isSystem = true, sortOrder = 3),
            CategoryEntity(name = "红包", type = "INCOME", icon = "redeem", color = 0xFFE91E63.toInt(), isSystem = true, sortOrder = 4),
            CategoryEntity(name = "其他", type = "INCOME", icon = "more_horiz", color = 0xFF607D8B.toInt(), isSystem = true, sortOrder = 5)
        )
        (defaultExpenseCategories + defaultIncomeCategories).forEach { categoryDao.insertCategory(it) }
    }

    private fun CategoryEntity.toDomain() = Category(
        id = this.id, name = this.name, type = CategoryType.valueOf(this.type),
        icon = this.icon, color = this.color, parentId = this.parentId, isSystem = this.isSystem
    )

    private fun Category.toEntity() = CategoryEntity(
        id = this.id, name = this.name, type = this.type.name,
        icon = this.icon, color = this.color, parentId = this.parentId, isSystem = this.isSystem
    )
}
