package com.example.pfinance.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.pfinance.domain.model.RecurringTransaction
import com.example.pfinance.ui.components.EmptyState
import com.example.pfinance.ui.components.formatCurrency
import com.example.pfinance.ui.components.formatDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecurringTransactionsScreen(
    navController: NavHostController,
    viewModel: RecurringTransactionsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("周期账单") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = { /* add */ }) {
                        Icon(Icons.Default.Add, contentDescription = "添加")
                    }
                }
            )
        }
    ) { padding ->
        if (state.recurringTransactions.isEmpty()) {
            Column(Modifier.fillMaxSize().padding(padding)) {
                EmptyState(Icons.Default.Loop, "没有周期账单", "添加房租、订阅等定期的收支")
            }
        } else {
            LazyColumn(
                Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.recurringTransactions) { item ->
                    Card(Modifier.fillMaxWidth()) {
                        Row(Modifier.padding(16.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Icon(
                                if (item.type.name == "INCOME") Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                                null,
                                tint = if (item.type.name == "INCOME")
                                    com.example.pfinance.ui.theme.IncomeGreen
                                else com.example.pfinance.ui.theme.ExpenseRed
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(item.note.ifBlank { item.categoryName }, style = MaterialTheme.typography.bodyLarge)
                                Text("下次: ${item.nextDate.format(DateTimeFormatter.ofPattern("MM月dd日"))}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Text(
                                "${if (item.type.name == "EXPENSE") "-" else "+"}${formatCurrency(item.amount)}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (item.type.name == "EXPENSE")
                                    com.example.pfinance.ui.theme.ExpenseRed
                                else com.example.pfinance.ui.theme.IncomeGreen
                            )
                        }
                    }
                }
            }
        }
    }
}
