package com.example.pfinance.ui.transaction

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.pfinance.domain.model.TransactionType
import com.example.pfinance.ui.components.*
import com.example.pfinance.ui.home.TransactionItem
import com.example.pfinance.ui.navigation.Screen
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    navController: NavHostController,
    viewModel: TransactionsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("账单", style = MaterialTheme.typography.titleLarge) },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Search.route) }) {
                        Icon(Icons.Default.Search, contentDescription = "搜索")
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            // Filters
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = state.selectedType == null,
                        onClick = { viewModel.filterByType(null) },
                        label = { Text("全部") }
                    )
                    FilterChip(
                        selected = state.selectedType == "EXPENSE",
                        onClick = { viewModel.filterByType("EXPENSE") },
                        label = { Text("支出") }
                    )
                    FilterChip(
                        selected = state.selectedType == "INCOME",
                        onClick = { viewModel.filterByType("INCOME") },
                        label = { Text("收入") }
                    )
                }
            }

            // Month summary
            Surface(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Row(
                    Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("支出", style = MaterialTheme.typography.labelSmall)
                        Text(
                            formatCurrency(state.monthlyExpense),
                            style = MaterialTheme.typography.titleMedium,
                            color = ExpenseRed,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("收入", style = MaterialTheme.typography.labelSmall)
                        Text(
                            formatCurrency(state.monthlyIncome),
                            style = MaterialTheme.typography.titleMedium,
                            color = IncomeGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("结余", style = MaterialTheme.typography.labelSmall)
                        Text(
                            formatCurrency(state.monthlyIncome - state.monthlyExpense),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // Transaction list
            if (state.transactions.isEmpty()) {
                EmptyState(
                    icon = Icons.Default.Receipt,
                    title = "暂无记录",
                    subtitle = "点击右下角 + 按钮开始记账"
                )
            } else {
                LazyColumn(contentPadding = PaddingValues(bottom = 80.dp)) {
                    items(state.transactions, key = { it.id }) { transaction ->
                        TransactionItem(transaction) {
                            navController.navigate(Screen.TransactionDetail.createRoute(transaction.id))
                        }
                    }
                }
            }
        }
    }
}
