package com.example.pfinance.ui.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pfinance.domain.model.Budget
import com.example.pfinance.domain.repository.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BudgetState(
    val budgets: List<Budget> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository
) : ViewModel() {

    private val _state = MutableStateFlow(BudgetState())
    val state: StateFlow<BudgetState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            budgetRepository.getActiveBudgets().collect { budgets ->
                _state.value = BudgetState(budgets = budgets, isLoading = false)
            }
        }
    }

    fun toggleBudget(budget: Budget) {
        viewModelScope.launch {
            budgetRepository.updateBudget(budget.copy(isActive = !budget.isActive))
        }
    }
}
