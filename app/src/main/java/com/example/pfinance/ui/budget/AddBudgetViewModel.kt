package com.example.pfinance.ui.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pfinance.domain.model.Budget
import com.example.pfinance.domain.model.BudgetPeriod
import com.example.pfinance.domain.model.BudgetType
import com.example.pfinance.domain.repository.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class AddBudgetState(
    val name: String = "",
    val amount: String = "",
    val period: BudgetPeriod = BudgetPeriod.MONTHLY,
    val alertThreshold: String = "80"
)

@HiltViewModel
class AddBudgetViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddBudgetState())
    val state: StateFlow<AddBudgetState> = _state.asStateFlow()

    fun setName(value: String) { _state.update { it.copy(name = value) } }
    fun setAmount(value: String) { _state.update { it.copy(amount = value) } }
    fun setPeriod(period: BudgetPeriod) { _state.update { it.copy(period = period) } }
    fun setAlertThreshold(value: String) { _state.update { it.copy(alertThreshold = value) } }

    fun save() {
        val s = _state.value
        val amount = s.amount.toDoubleOrNull() ?: return
        val threshold = (s.alertThreshold.toDoubleOrNull() ?: 80.0) / 100.0

        viewModelScope.launch {
            budgetRepository.insertBudget(Budget(
                name = s.name.ifBlank { "月度预算" },
                type = BudgetType.TOTAL,
                period = s.period,
                amount = amount,
                startDate = LocalDateTime.now(),
                alertThreshold = threshold
            ))
        }
    }
}
