package com.example.pfinance.ui.navigation

import androidx.compose.animation.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pfinance.ui.home.HomeScreen
import com.example.pfinance.ui.transaction.TransactionsScreen
import com.example.pfinance.ui.transaction.AddTransactionScreen
import com.example.pfinance.ui.transaction.TransactionDetailScreen
import com.example.pfinance.ui.transaction.SearchScreen
import com.example.pfinance.ui.statistics.StatisticsScreen
import com.example.pfinance.ui.budget.BudgetScreen
import com.example.pfinance.ui.budget.AddBudgetScreen
import com.example.pfinance.ui.account.AccountsScreen
import com.example.pfinance.ui.account.AddAccountScreen
import com.example.pfinance.ui.account.AccountDetailScreen
import com.example.pfinance.ui.account.TransferScreen
import com.example.pfinance.ui.settings.*

@Composable
fun MainNavGraph() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = currentDestination?.route in bottomNavItems.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    tonalElevation = 3.dp
                ) {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) },
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (showBottomBar) {
                FloatingActionButton(
                    onClick = { navController.navigate(Screen.AddTransaction.createRoute()) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "记一笔")
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { fadeIn(initialAlpha = 0.3f) },
            exitTransition = { fadeOut(targetAlpha = 0.3f) }
        ) {
            composable(Screen.Home.route) { HomeScreen(navController) }
            composable(Screen.Transactions.route) { TransactionsScreen(navController) }
            composable(
                Screen.AddTransaction.route,
                arguments = listOf(
                    navArgument("type") { type = NavType.StringType; defaultValue = "EXPENSE" },
                    navArgument("id") { type = NavType.LongType; defaultValue = -1L }
                )
            ) { backStackEntry ->
                val type = backStackEntry.arguments?.getString("type") ?: "EXPENSE"
                val id = backStackEntry.arguments?.getLong("id") ?: -1L
                AddTransactionScreen(navController, type, id)
            }
            composable(
                Screen.TransactionDetail.route,
                arguments = listOf(navArgument("id") { type = NavType.LongType })
            ) { TransactionDetailScreen(navController) }
            composable(Screen.Search.route) { SearchScreen(navController) }
            composable(Screen.Statistics.route) { StatisticsScreen(navController) }
            composable(Screen.Budget.route) { BudgetScreen(navController) }
            composable(Screen.AddBudget.route) { AddBudgetScreen(navController) }
            composable(Screen.Accounts.route) { AccountsScreen(navController) }
            composable(
                Screen.AddAccount.route,
                arguments = listOf(navArgument("id") { type = NavType.LongType; defaultValue = -1L })
            ) { backStackEntry ->
                val accountId = backStackEntry.arguments?.getLong("id") ?: -1L
                AddAccountScreen(navController, accountId)
            }
            composable(
                Screen.AccountDetail.route,
                arguments = listOf(navArgument("id") { type = NavType.LongType })
            ) { AccountDetailScreen(navController) }
            composable(Screen.Transfer.route) { TransferScreen(navController) }
            composable(Screen.Categories.route) { CategoriesScreen(navController) }
            composable(Screen.RecurringTransactions.route) { RecurringTransactionsScreen(navController) }
            composable(Screen.SavingGoals.route) { SavingGoalsScreen(navController) }
            composable(Screen.Settings.route) { SettingsScreen(navController) }
            composable(Screen.BackupRestore.route) { BackupRestoreScreen(navController) }
            composable(Screen.DataExport.route) { DataExportScreen(navController) }
            composable(Screen.DataImport.route) { DataImportScreen(navController) }
            composable(Screen.Reimbursements.route) { ReimbursementsScreen(navController) }
            composable(Screen.SharedLedgers.route) { SharedLedgersScreen(navController) }
        }
    }
}
