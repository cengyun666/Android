package com.example.pfinance.ui.account

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pfinance.domain.model.Account
import com.example.pfinance.domain.model.Transaction
import com.example.pfinance.domain.repository.AccountRepository
import com.example.pfinance.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AccountDetailState(
    val account: Account? = null,
    val recentTransactions: List<Transaction> = emptyList()
)

@HiltViewModel
class AccountDetailViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val accountId: Long = savedStateHandle.get<Long>("id") ?: 0

    private val _state = MutableStateFlow(AccountDetailState())
    val state: StateFlow<AccountDetailState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val account = accountRepository.getAccountById(accountId)
            _state.value = _state.value.copy(account = account)

            transactionRepository.getTransactionsByAccount(accountId).collect { transactions ->
                _state.update { it.copy(recentTransactions = transactions.take(20)) }
            }
        }
    }
}
