package com.example.pfinance.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Transactions : Screen("transactions")
    data object AddTransaction : Screen("add_transaction?type={type}&id={id}") {
        fun createRoute(type: String = "EXPENSE", id: Long = -1) = "add_transaction?type=$type&id=$id"
    }
    data object TransactionDetail : Screen("transaction_detail/{id}") {
        fun createRoute(id: Long) = "transaction_detail/$id"
    }
    data object Search : Screen("search")
    data object Statistics : Screen("statistics")
    data object Budget : Screen("budget")
    data object AddBudget : Screen("add_budget")
    data object Accounts : Screen("accounts")
    data object AddAccount : Screen("add_account/{id}") {
        fun createRoute(id: Long = -1) = "add_account/$id"
    }
    data object AccountDetail : Screen("account_detail/{id}") {
        fun createRoute(id: Long) = "account_detail/$id"
    }
    data object Transfer : Screen("transfer")
    data object Categories : Screen("categories")
    data object RecurringTransactions : Screen("recurring_transactions")
    data object SavingGoals : Screen("saving_goals")
    data object Settings : Screen("settings")
    data object BackupRestore : Screen("backup_restore")
    data object DataExport : Screen("data_export")
    data object DataImport : Screen("data_import")
    data object Reimbursements : Screen("reimbursements")
    data object SharedLedgers : Screen("shared_ledgers")
    data object AppLock : Screen("app_lock")
}

data class BottomNavItem(
    val label: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem("首页", Screen.Home.route, Icons.Filled.Home, Icons.Outlined.Home),
    BottomNavItem("账单", Screen.Transactions.route, Icons.Filled.Receipt, Icons.Outlined.Receipt),
    BottomNavItem("统计", Screen.Statistics.route, Icons.Filled.PieChart, Icons.Outlined.PieChart),
    BottomNavItem("预算", Screen.Budget.route, Icons.Filled.Savings, Icons.Outlined.Savings),
    BottomNavItem("我的", Screen.Accounts.route, Icons.Filled.AccountBalanceWallet, Icons.Outlined.AccountBalanceWallet)
)
