package com.example.pfinance.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pfinance.domain.model.Transaction
import com.example.pfinance.domain.model.TransactionType
import com.example.pfinance.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class TransactionsState(
    val transactions: List<Transaction> = emptyList(),
    val selectedType: String? = null,
    val monthlyIncome: Double = 0.0,
    val monthlyExpense: Double = 0.0,
    val isLoading: Boolean = true
)

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionsState())
    val state: StateFlow<TransactionsState> = _state.asStateFlow()

    private val _filterType = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch {
            _filterType.flatMapLatest { type ->
                if (type != null) transactionRepository.getTransactionsByType(TransactionType.valueOf(type))
                else transactionRepository.getAllTransactions()
            }.collect { transactions ->
                val now = LocalDateTime.now()
                val monthStart = now.withDayOfMonth(1).withHour(0).withMinute(0)
                val monthEnd = now.withDayOfMonth(now.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59)
                _state.value = TransactionsState(
                    transactions = transactions,
                    selectedType = _filterType.value,
                    monthlyIncome = transactions.filter { it.type == TransactionType.INCOME && it.date >= monthStart && it.date <= monthEnd }.sumOf { it.amount },
                    monthlyExpense = transactions.filter { it.type == TransactionType.EXPENSE && it.date >= monthStart && it.date <= monthEnd }.sumOf { it.amount },
                    isLoading = false
                )
            }
        }
    }

    fun filterByType(type: String?) {
        _filterType.value = type
    }
}
