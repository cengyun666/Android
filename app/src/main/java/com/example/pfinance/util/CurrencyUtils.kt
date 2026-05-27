package com.example.pfinance.util

import java.text.DecimalFormat

object CurrencyUtils {
    private val symbols = mapOf(
        "CNY" to "¥", "USD" to "$", "EUR" to "€", "JPY" to "¥",
        "GBP" to "£", "HKD" to "HK$", "TWD" to "NT$", "KRW" to "₩"
    )

    fun getSymbol(currencyCode: String): String = symbols[currencyCode] ?: currencyCode

    fun format(amount: Double, currencyCode: String = "CNY"): String {
        val formatter = DecimalFormat("#,##0.00")
        return "${getSymbol(currencyCode)}${formatter.format(amount)}"
    }

    fun formatShort(amount: Double, currencyCode: String = "CNY"): String {
        val symbol = getSymbol(currencyCode)
        return when {
            amount >= 10000 -> {
                val formatter = DecimalFormat("#,##0.0")
                "$symbol${formatter.format(amount / 10000)}万"
            }
            else -> {
                val formatter = DecimalFormat("#,##0")
                "$symbol${formatter.format(amount)}"
            }
        }
    }
}
