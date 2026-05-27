package com.example.pfinance.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pfinance.domain.model.Category
import com.example.pfinance.domain.model.CategoryType
import com.example.pfinance.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoriesState(
    val expenseCategories: List<Category> = emptyList(),
    val incomeCategories: List<Category> = emptyList()
)

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CategoriesState())
    val state: StateFlow<CategoriesState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            categoryRepository.getCategoriesByType(CategoryType.EXPENSE).collect { categories ->
                _state.update { it.copy(expenseCategories = categories) }
            }
        }
        viewModelScope.launch {
            categoryRepository.getCategoriesByType(CategoryType.INCOME).collect { categories ->
                _state.update { it.copy(incomeCategories = categories) }
            }
        }
    }
}
