package com.example.pfinance.ui.account

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
import com.example.pfinance.domain.model.AccountType
import com.example.pfinance.domain.model.TransactionType
import com.example.pfinance.ui.components.*
import com.example.pfinance.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailScreen(navController: NavHostController, viewModel: AccountDetailViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.account?.name ?: "账户详情") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    state.account?.let { account ->
                        IconButton(onClick = {
                            navController.navigate(Screen.AddAccount.createRoute(account.id))
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "编辑")
                        }
                    }
                }
            )
        }
    ) { padding ->
        val account = state.account ?: return@Scaffold

        LazyColumn(
            Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Balance card
            item {
                Card(
                    Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("当前余额", style = MaterialTheme.typography.labelLarge)
                        Spacer(Modifier.height(8.dp))
                        Text(formatCurrency(account.balance),
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold)
                        if (account.type == AccountType.CREDIT_CARD && account.creditLimit != null) {
                            Spacer(Modifier.height(4.dp))
                            Text("额度: ${formatCurrency(account.creditLimit)}",
                                style = MaterialTheme.typography.bodySmall)
                            Text("可用: ${formatCurrency(account.creditLimit + account.balance)}",
                                style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            // Account info
            item {
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        DetailRow("类型", account.type.name)
                        DetailRow("币种", account.currencyCode)
                        if (account.note.isNotEmpty()) DetailRow("备注", account.note)
                        if (account.billDate != null) DetailRow("账单日", "每月${account.billDate}日")
                        if (account.dueDate != null) DetailRow("还款日", "每月${account.dueDate}日")
                    }
                }
            }

            // Recent transactions
            item {
                SectionHeader("最近记录") {
                    TextButton(onClick = { navController.navigate(Screen.Transactions.route) }) {
                        Text("全部")
                    }
                }
            }

            if (state.recentTransactions.isEmpty()) {
                item {
                    EmptyState(Icons.Default.Receipt, "暂无记录")
                }
            } else {
                items(state.recentTransactions) { tx ->
                    com.example.pfinance.ui.home.TransactionItem(tx) {
                        navController.navigate(Screen.TransactionDetail.createRoute(tx.id))
                    }
                }
            }
        }
    }
}
