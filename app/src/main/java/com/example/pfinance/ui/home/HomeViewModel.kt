package com.example.pfinance.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pfinance.data.local.AppDatabase
import com.example.pfinance.domain.model.*
import com.example.pfinance.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class HomeState(
    val isLoading: Boolean = true,
    val monthlyIncome: Double = 0.0,
    val monthlyExpense: Double = 0.0,
    val netWorth: Double = 0.0,
    val budgets: List<Budget> = emptyList(),
    val recentTransactions: List<Transaction> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val budgetRepository: BudgetRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    fun init(context: Context) {
        viewModelScope.launch {
            categoryRepository.initDefaultCategories()

            val now = LocalDateTime.now()
            val monthStart = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0)
            val monthEnd = now.withDayOfMonth(now.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59)

            val monthlyIncome = transactionRepository.getTotalByTypeAndDateRange(
                TransactionType.INCOME, monthStart, monthEnd
            )
            val monthlyExpense = transactionRepository.getTotalByTypeAndDateRange(
                TransactionType.EXPENSE, monthStart, monthEnd
            )
            val totalAssets = accountRepository.getTotalAssets()
            val totalLiabilities = accountRepository.getTotalLiabilities()

            val budgets = budgetRepository.getActiveBudgets().first()
            val transactions = transactionRepository.getAllTransactions().first().take(10)

            _state.value = HomeState(
                isLoading = false,
                monthlyIncome = monthlyIncome,
                monthlyExpense = monthlyExpense,
                netWorth = totalAssets - totalLiabilities,
                budgets = budgets,
                recentTransactions = transactions
            )
        }

        // Observe real-time changes
        viewModelScope.launch {
            transactionRepository.getAllTransactions().collect { transactions ->
                val now = LocalDateTime.now()
                val monthStart = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0)
                val monthEnd = now.withDayOfMonth(now.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59)
                _state.value = _state.value.copy(
                    monthlyIncome = transactions.filter { it.type == TransactionType.INCOME && it.date >= monthStart && it.date <= monthEnd }.sumOf { it.amount },
                    monthlyExpense = transactions.filter { it.type == TransactionType.EXPENSE && it.date >= monthStart && it.date <= monthEnd }.sumOf { it.amount },
                    recentTransactions = transactions.take(10)
                )
            }
        }
    }
}
