package com.example.pfinance.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pfinance.domain.model.Account
import com.example.pfinance.domain.repository.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AccountsState(
    val accounts: List<Account> = emptyList(),
    val totalAssets: Double = 0.0,
    val totalLiabilities: Double = 0.0,
    val netWorth: Double = 0.0,
    val isLoading: Boolean = true
)

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AccountsState())
    val state: StateFlow<AccountsState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            accountRepository.getVisibleAccounts().collect { accounts ->
                val totalAssets = accountRepository.getTotalAssets()
                val totalLiabilities = accountRepository.getTotalLiabilities()
                _state.value = AccountsState(
                    accounts = accounts,
                    totalAssets = totalAssets,
                    totalLiabilities = totalLiabilities,
                    netWorth = totalAssets - totalLiabilities,
                    isLoading = false
                )
            }
        }
    }
}
