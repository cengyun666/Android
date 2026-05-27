package com.example.pfinance.ui.account

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.pfinance.domain.model.Account
import com.example.pfinance.ui.components.*
import com.example.pfinance.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsScreen(navController: NavHostController, viewModel: AccountsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("我的", style = MaterialTheme.typography.titleLarge) },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                        Icon(Icons.Default.Settings, contentDescription = "设置")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Net worth overview
            item {
                Card(
                    Modifier.fillMaxWidth().padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(Modifier.padding(20.dp)) {
                        Text("净资产", style = MaterialTheme.typography.labelLarge)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            formatCurrency(state.netWorth),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Column {
                                Text("总资产", style = MaterialTheme.typography.labelSmall)
                                Text(formatCurrencyShort(state.totalAssets),
                                    style = MaterialTheme.typography.titleMedium, color = IncomeGreen)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("总负债", style = MaterialTheme.typography.labelSmall)
                                Text(formatCurrencyShort(state.totalLiabilities),
                                    style = MaterialTheme.typography.titleMedium, color = ExpenseRed)
                            }
                        }
                    }
                }
            }

            // Accounts list
            item {
                Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    Text("账户", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    TextButton(onClick = { navController.navigate(Screen.AddAccount.createRoute()) }) {
                        Icon(Icons.Default.Add, null, Modifier.size(18.dp))
                        Text("添加")
                    }
                }
            }

            if (state.accounts.isEmpty()) {
                item {
                    EmptyState(
                        icon = Icons.Default.AccountBalanceWallet,
                        title = "还没有添加账户",
                        subtitle = "添加你的银行卡、支付宝等账户"
                    )
                }
            } else {
                items(state.accounts) { account ->
                    AccountItem(account) {
                        navController.navigate(Screen.AccountDetail.createRoute(account.id))
                    }
                }
            }

            // Quick links
            item { Spacer(Modifier.height(16.dp)) }
            item { SectionHeader("更多功能") }
            item {
                SettingsMenuItem(Icons.Default.Category, "分类管理") { navController.navigate(Screen.Categories.route) }
                SettingsMenuItem(Icons.Default.Loop, "周期账单") { navController.navigate(Screen.RecurringTransactions.route) }
                SettingsMenuItem(Icons.Default.Flag, "存钱目标") { navController.navigate(Screen.SavingGoals.route) }
                SettingsMenuItem(Icons.Default.ReceiptLong, "报销管理") { navController.navigate(Screen.Reimbursements.route) }
                SettingsMenuItem(Icons.Default.Group, "共享账本") { navController.navigate(Screen.SharedLedgers.route) }
            }
        }
    }
}

@Composable
fun AccountItem(account: Account, onClick: () -> Unit) {
    Card(
        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp).clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(
                Modifier.size(44.dp), shape = MaterialTheme.shapes.small,
                color = Color(account.color).copy(alpha = 0.12f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(iconNameToVector(account.icon), null, tint = Color(account.color))
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(account.name, style = MaterialTheme.typography.bodyLarge)
                Text(account.type.name, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(
                formatCurrency(account.balance),
                style = MaterialTheme.typography.titleMedium,
                color = if (account.balance >= 0) IncomeGreen else ExpenseRed,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
