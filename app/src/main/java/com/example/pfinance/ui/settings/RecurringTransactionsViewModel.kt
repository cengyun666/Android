package com.example.pfinance.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pfinance.data.local.dao.RecurringTransactionDao
import com.example.pfinance.domain.model.RecurringFrequency
import com.example.pfinance.domain.model.RecurringTransaction
import com.example.pfinance.domain.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecurringTransactionsState(
    val recurringTransactions: List<RecurringTransaction> = emptyList()
)

@HiltViewModel
class RecurringTransactionsViewModel @Inject constructor(
    private val recurringDao: RecurringTransactionDao
) : ViewModel() {

    private val _state = MutableStateFlow(RecurringTransactionsState())
    val state: StateFlow<RecurringTransactionsState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            recurringDao.getAllRecurringTransactions().collect { list ->
                _state.value = RecurringTransactionsState(
                    recurringTransactions = list.map { entity ->
                        RecurringTransaction(
                            id = entity.id,
                            type = TransactionType.valueOf(entity.type),
                            amount = entity.amount,
                            accountId = entity.accountId,
                            categoryId = entity.categoryId,
                            frequency = RecurringFrequency.valueOf(entity.frequency),
                            interval = entity.interval,
                            nextDate = entity.nextDate,
                            endDate = entity.endDate,
                            note = entity.note,
                            isActive = entity.isActive,
                            autoCreate = entity.autoCreate,
                            remindBefore = entity.remindBefore
                        )
                    }
                )
            }
        }
    }
}
