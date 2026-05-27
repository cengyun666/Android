package com.example.pfinance.ui.budget

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
import com.example.pfinance.domain.model.Budget
import com.example.pfinance.ui.components.*
import com.example.pfinance.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(navController: NavHostController, viewModel: BudgetViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("预算", style = MaterialTheme.typography.titleLarge) },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.AddBudget.route) }) {
                        Icon(Icons.Default.Add, contentDescription = "添加预算")
                    }
                }
            )
        }
    ) { padding ->
        if (state.budgets.isEmpty()) {
            Column(Modifier.fillMaxSize().padding(padding)) {
                EmptyState(
                    icon = Icons.Default.Savings,
                    title = "还没有设置预算",
                    subtitle = "设置预算可以帮助你更好地管理支出"
                ) {
                    Button(onClick = { navController.navigate(Screen.AddBudget.route) }) {
                        Text("添加预算")
                    }
                }
            }
        } else {
            LazyColumn(
                Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Total budget summary
                item {
                    val totalBudget = state.budgets.sumOf { it.amount }
                    val totalSpent = state.budgets.sumOf { it.spent }
                    Card(
                        Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(Modifier.padding(20.dp)) {
                            Text("总预算", style = MaterialTheme.typography.labelLarge)
                            Spacer(Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text(formatCurrencyShort(totalSpent), style = MaterialTheme.typography.headlineSmall)
                                Text("/ ${formatCurrencyShort(totalBudget)}", style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer)
                            }
                            Spacer(Modifier.height(8.dp))
                            val totalProgress = if (totalBudget > 0) (totalSpent / totalBudget).toFloat() else 0f
                            ProgressBar(progress = totalProgress.coerceIn(0f, 1.5f))
                            Spacer(Modifier.height(4.dp))
                            Text("${(totalProgress * 100).toInt()}% 已使用",
                                style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

                items(state.budgets) { budget ->
                    BudgetCard(budget) {
                        viewModel.toggleBudget(budget)
                    }
                }
            }
        }
    }
}

@Composable
fun BudgetCard(budget: Budget, onClick: () -> Unit) {
    Card(Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(budget.name, style = MaterialTheme.typography.titleSmall)
                    Text(
                        when (budget.period) {
                            com.example.pfinance.domain.model.BudgetPeriod.MONTHLY -> "月度预算"
                            com.example.pfinance.domain.model.BudgetPeriod.WEEKLY -> "周预算"
                            com.example.pfinance.domain.model.BudgetPeriod.YEARLY -> "年度预算"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(formatCurrencyShort(budget.spent), style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold)
                    Text("/ ${formatCurrencyShort(budget.amount)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Spacer(Modifier.height(8.dp))
            ProgressBar(
                progress = budget.progress,
                color = if (budget.isOverBudget) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(4.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    "${(budget.progress * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (budget.isOverBudget) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
                val remaining = budget.remaining
                Text(
                    if (remaining > 0) "剩余 ${formatCurrencyShort(remaining)}" else "超支 ${formatCurrencyShort(-remaining)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (remaining > 0) IncomeGreen else ExpenseRed
                )
            }
        }
    }
}
