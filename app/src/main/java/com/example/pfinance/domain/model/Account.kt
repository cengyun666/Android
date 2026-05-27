package com.example.pfinance.domain.model

data class Account(
    val id: Long = 0,
    val name: String,
    val type: AccountType,
    val balance: Double = 0.0,
    val initialBalance: Double = 0.0,
    val currencyCode: String = "CNY",
    val icon: String = "account_balance_wallet",
    val color: Int = 0xFF4CAF50.toInt(),
    val creditLimit: Double? = null,
    val billDate: Int? = null,
    val dueDate: Int? = null,
    val cardNumber: String = "",
    val note: String = "",
    val isHidden: Boolean = false,
    val isArchived: Boolean = false
)

enum class AccountType {
    CASH, SAVINGS, CREDIT_CARD, ALIPAY, WECHAT, INVESTMENT, STORED_VALUE, CUSTOM
}
