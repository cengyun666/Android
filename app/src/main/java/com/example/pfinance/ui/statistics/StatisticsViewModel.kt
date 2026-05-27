package com.example.pfinance.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pfinance.domain.model.*
import com.example.pfinance.domain.repository.StatisticsRepository
import com.example.pfinance.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class StatisticsState(
    val isLoading: Boolean = true,
    val selectedPeriod: String = "MONTH",
    val totalExpense: Double = 0.0,
    val totalIncome: Double = 0.0,
    val categorySummaries: List<CategorySummary> = emptyList(),
    val monthlySummaries: List<MonthlySummary> = emptyList(),
    val merchantSummaries: List<MerchantSummary> = emptyList()
)

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val statisticsRepository: StatisticsRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _state = MutableStateFlow(StatisticsState())
    val state: StateFlow<StatisticsState> = _state.asStateFlow()

    init {
        selectPeriod("MONTH")
    }

    fun selectPeriod(period: String) {
        _state.update { it.copy(selectedPeriod = period, isLoading = true) }
        val now = LocalDateTime.now()
        val (start, end) = when (period) {
            "MONTH" -> now.withDayOfMonth(1).withHour(0).withMinute(0) to
                now.withDayOfMonth(now.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59)
            "3MONTH" -> now.minusMonths(3).withDayOfMonth(1).withHour(0).withMinute(0) to now
            "YEAR" -> now.withDayOfYear(1).withHour(0).withMinute(0) to now
            else -> now.withDayOfMonth(1).withHour(0).withMinute(0) to now
        }

        viewModelScope.launch {
            val totalExpense = transactionRepository.getTotalByTypeAndDateRange(TransactionType.EXPENSE, start, end)
            val totalIncome = transactionRepository.getTotalByTypeAndDateRange(TransactionType.INCOME, start, end)
            val categorySummaries = statisticsRepository.getCategorySummary(TransactionType.EXPENSE, start, end).first()
            val monthlySummaries = statisticsRepository.getMonthlySummary(now.year).first()
            val merchantSummaries = statisticsRepository.getMerchantSummaries(start, end).first()

            _state.update {
                it.copy(
                    isLoading = false,
                    totalExpense = totalExpense,
                    totalIncome = totalIncome,
                    categorySummaries = categorySummaries,
                    monthlySummaries = monthlySummaries,
                    merchantSummaries = merchantSummaries
                )
            }
        }
    }
}
