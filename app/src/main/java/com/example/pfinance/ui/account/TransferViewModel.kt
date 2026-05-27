package com.example.pfinance.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pfinance.domain.model.Account
import com.example.pfinance.domain.model.Transaction
import com.example.pfinance.domain.model.TransactionType
import com.example.pfinance.domain.repository.AccountRepository
import com.example.pfinance.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class TransferState(
    val amount: String = "",
    val fromAccountId: Long? = null,
    val toAccountId: Long? = null,
    val fee: String = "",
    val note: String = "",
    val accounts: List<Account> = emptyList()
)

@HiltViewModel
class TransferViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TransferState())
    val state: StateFlow<TransferState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val accounts = accountRepository.getVisibleAccounts().first()
            _state.value = _state.value.copy(
                accounts = accounts,
                fromAccountId = accounts.firstOrNull()?.id
            )
        }
    }

    fun setAmount(value: String) { _state.update { it.copy(amount = value) } }
    fun setFromAccount(id: Long) { _state.update { it.copy(fromAccountId = id) } }
    fun setToAccount(id: Long) { _state.update { it.copy(toAccountId = id) } }
    fun setFee(value: String) { _state.update { it.copy(fee = value) } }
    fun setNote(value: String) { _state.update { it.copy(note = value) } }

    fun executeTransfer() {
        val s = _state.value
        val amount = s.amount.toDoubleOrNull() ?: return
        val fromId = s.fromAccountId ?: return
        val toId = s.toAccountId ?: return
        val fee = s.fee.toDoubleOrNull() ?: 0.0

        viewModelScope.launch {
            // Deduct from source account (including fee)
            accountRepository.updateBalance(fromId, -amount - fee)
            // Add to destination account
            accountRepository.updateBalance(toId, amount)
            // Record transfer
            transactionRepository.insertTransaction(Transaction(
                type = TransactionType.TRANSFER,
                amount = amount,
                accountId = fromId,
                toAccountId = toId,
                date = LocalDateTime.now(),
                note = s.note,
                fee = fee
            ))
        }
    }
}
