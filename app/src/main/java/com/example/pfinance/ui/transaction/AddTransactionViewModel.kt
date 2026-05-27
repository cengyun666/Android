package com.example.pfinance.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pfinance.domain.model.*
import com.example.pfinance.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class AddTransactionState(
    val transactionType: String = "EXPENSE",
    val amount: String = "",
    val selectedCategoryId: Long? = null,
    val selectedAccountId: Long? = null,
    val selectedToAccountId: Long? = null,
    val date: LocalDateTime = LocalDateTime.now(),
    val note: String = "",
    val tags: String = "",
    val isReimbursable: Boolean = false,
    val fee: String = "",
    val merchantName: String = "",
    val categories: List<Category> = emptyList(),
    val accounts: List<Account> = emptyList(),
    val isLoading: Boolean = false,
    val showDatePickerDialog: Boolean = false
)

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddTransactionState())
    val state: StateFlow<AddTransactionState> = _state.asStateFlow()

    private var editingTransactionId: Long = -1

    fun init(type: String, editId: Long) {
        editingTransactionId = editId
        viewModelScope.launch {
            val accounts = accountRepository.getVisibleAccounts().first()
            val categoriesByType = if (type == "INCOME") CategoryType.INCOME else CategoryType.EXPENSE
            val categories = categoryRepository.getCategoriesByType(categoriesByType).first()
            _state.value = _state.value.copy(
                transactionType = type,
                accounts = accounts,
                categories = categories,
                selectedAccountId = accounts.firstOrNull()?.id,
                selectedCategoryId = categories.firstOrNull()?.id
            )

            // Load existing transaction for editing
            if (editId > 0) {
                val tx = transactionRepository.getTransactionById(editId)
                if (tx != null) {
                    _state.value = _state.value.copy(
                        transactionType = tx.type.name,
                        amount = if (tx.amount > 0) tx.amount.toString() else "",
                        selectedCategoryId = tx.categoryId,
                        selectedAccountId = tx.accountId,
                        selectedToAccountId = tx.toAccountId,
                        date = tx.date,
                        note = tx.note,
                        tags = tx.tags.joinToString(","),
                        isReimbursable = tx.isReimbursable,
                        fee = if (tx.fee > 0) tx.fee.toString() else "",
                        merchantName = tx.merchantName,
                        categories = categories,
                        accounts = accounts
                    )
                }
            }
        }
    }

    fun setType(type: String) {
        _state.value = _state.value.copy(transactionType = type)
        viewModelScope.launch {
            val categoriesByType = if (type == "INCOME") CategoryType.INCOME else CategoryType.EXPENSE
            val categories = categoryRepository.getCategoriesByType(categoriesByType).first()
            _state.value = _state.value.copy(
                categories = categories,
                selectedCategoryId = categories.firstOrNull()?.id
            )
        }
    }

    fun setAmount(value: String) { _state.update { it.copy(amount = value) } }
    fun selectCategory(id: Long) { _state.update { it.copy(selectedCategoryId = id) } }
    fun selectAccount(id: Long) { _state.update { it.copy(selectedAccountId = id) } }
    fun selectToAccount(id: Long) { _state.update { it.copy(selectedToAccountId = id) } }
    fun setNote(value: String) { _state.update { it.copy(note = value) } }
    fun setTags(value: String) { _state.update { it.copy(tags = value) } }
    fun setFee(value: String) { _state.update { it.copy(fee = value) } }
    fun setMerchantName(value: String) { _state.update { it.copy(merchantName = value) } }
    fun setReimbursable(value: Boolean) { _state.update { it.copy(isReimbursable = value) } }
    fun showDatePicker() { _state.update { it.copy(showDatePickerDialog = true) } }
    fun hideDatePicker() { _state.update { it.copy(showDatePickerDialog = false) } }

    fun saveTransaction() {
        val s = _state.value
        val amount = s.amount.toDoubleOrNull() ?: return
        val accountId = s.selectedAccountId ?: return
        if (amount <= 0) return

        viewModelScope.launch {
            val transaction = Transaction(
                id = editingTransactionId,
                type = TransactionType.valueOf(s.transactionType),
                amount = amount,
                accountId = accountId,
                toAccountId = s.selectedToAccountId,
                categoryId = s.selectedCategoryId,
                date = s.date,
                note = s.note,
                tags = s.tags.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                isReimbursable = s.isReimbursable,
                fee = s.fee.toDoubleOrNull() ?: 0.0,
                merchantName = s.merchantName
            )

            if (editingTransactionId > 0) {
                transactionRepository.updateTransaction(transaction)
            } else {
                val txId = transactionRepository.insertTransaction(transaction)

                // Update account balance
                when (s.transactionType) {
                    "EXPENSE" -> accountRepository.updateBalance(accountId, -amount - (s.fee.toDoubleOrNull() ?: 0.0))
                    "INCOME" -> accountRepository.updateBalance(accountId, amount)
                    "TRANSFER" -> {
                        val toAccountId = s.selectedToAccountId
                        val fee = s.fee.toDoubleOrNull() ?: 0.0
                        accountRepository.updateBalance(accountId, -amount - fee)
                        if (toAccountId != null) {
                            accountRepository.updateBalance(toAccountId, amount)
                        }
                    }
                }
            }
        }
    }
}
