package com.example.pfinance.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pfinance.domain.model.Account
import com.example.pfinance.domain.model.AccountType
import com.example.pfinance.domain.repository.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddAccountState(
    val isEdit: Boolean = false,
    val name: String = "",
    val type: AccountType = AccountType.CASH,
    val balance: String = "0",
    val creditLimit: String = "",
    val billDate: String = "",
    val dueDate: String = "",
    val note: String = ""
)

@HiltViewModel
class AddAccountViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddAccountState())
    val state: StateFlow<AddAccountState> = _state.asStateFlow()

    private var editingId: Long = -1

    fun init(accountId: Long) {
        if (accountId > 0) {
            editingId = accountId
            viewModelScope.launch {
                val account = accountRepository.getAccountById(accountId)
                if (account != null) {
                    _state.value = AddAccountState(
                        isEdit = true,
                        name = account.name,
                        type = account.type,
                        balance = account.initialBalance.toString(),
                        creditLimit = account.creditLimit?.toString() ?: "",
                        billDate = account.billDate?.toString() ?: "",
                        dueDate = account.dueDate?.toString() ?: "",
                        note = account.note
                    )
                }
            }
        }
    }

    fun setName(value: String) { _state.update { it.copy(name = value) } }
    fun setType(type: AccountType) { _state.update { it.copy(type = type) } }
    fun setBalance(value: String) { _state.update { it.copy(balance = value) } }
    fun setCreditLimit(value: String) { _state.update { it.copy(creditLimit = value) } }
    fun setBillDate(value: String) { _state.update { it.copy(billDate = value) } }
    fun setDueDate(value: String) { _state.update { it.copy(dueDate = value) } }
    fun setNote(value: String) { _state.update { it.copy(note = value) } }

    fun save() {
        val s = _state.value
        val balance = s.balance.toDoubleOrNull() ?: 0.0
        viewModelScope.launch {
            val account = Account(
                id = if (editingId > 0) editingId else 0,
                name = s.name.ifBlank { s.type.name },
                type = s.type,
                initialBalance = balance,
                balance = balance,
                creditLimit = s.creditLimit.toDoubleOrNull(),
                billDate = s.billDate.toIntOrNull(),
                dueDate = s.dueDate.toIntOrNull(),
                note = s.note
            )
            if (editingId > 0) {
                accountRepository.updateAccount(account)
            } else {
                accountRepository.insertAccount(account)
            }
        }
    }
}
