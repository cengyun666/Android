package com.example.pfinance.ui.statistics

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.pfinance.domain.model.*
import com.example.pfinance.ui.components.*
import com.example.pfinance.ui.theme.ExpenseRed
import com.example.pfinance.ui.theme.IncomeGreen
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    navController: NavHostController,
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("统计", style = MaterialTheme.typography.titleLarge) })
        }
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Period selector
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("本月" to "MONTH", "近3月" to "3MONTH", "今年" to "YEAR").forEach { (label, period) ->
                            FilterChip(
                                selected = state.selectedPeriod == period,
                                onClick = { viewModel.selectPeriod(period) },
                                label = { Text(label) }
                            )
                        }
                    }
                }

                // Summary card
                item {
                    Card(
                        Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            SummaryItem("支出", state.totalExpense, ExpenseRed)
                            SummaryItem("收入", state.totalIncome, IncomeGreen)
                            SummaryItem("结余", state.totalIncome - state.totalExpense,
                                if (state.totalIncome >= state.totalExpense) IncomeGreen else ExpenseRed)
                        }
                    }
                }

                // Category pie chart (simplified bar representation)
                if (state.categorySummaries.isNotEmpty()) {
                    item {
                        Text("支出分类排行", style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold)
                    }
                    item {
                        Card(Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp)) {
                                state.categorySummaries.forEach { summary ->
                                    CategoryBarItem(summary)
                                    Spacer(Modifier.height(8.dp))
                                }
                            }
                        }
                    }
                }

                // Monthly trend
                if (state.monthlySummaries.isNotEmpty()) {
                    item {
                        Text("月度趋势", style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold)
                    }
                    item {
                        Card(Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp)) {
                                state.monthlySummaries.takeLast(6).forEach { summary ->
                                    MonthlyTrendItem(summary)
                                    Spacer(Modifier.height(8.dp))
                                }
                            }
                        }
                    }
                }

                // Merchant analysis
                if (state.merchantSummaries.isNotEmpty()) {
                    item {
                        Text("消费商家排行", style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold)
                    }
                    items(state.merchantSummaries) { merchant ->
                        Card(Modifier.fillMaxWidth()) {
                            Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Store, null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(merchant.merchantName, style = MaterialTheme.typography.bodyLarge)
                                    Text("${merchant.count}笔", style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Text(formatCurrency(merchant.totalAmount),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = ExpenseRed)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryItem(label: String, amount: Double, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelSmall)
        Text(formatCurrencyShort(amount), style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
fun CategoryBarItem(summary: CategorySummary) {
    val maxWidth = 1f.coerceAtMost(summary.percentage)
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CategoryIcon(icon = summary.categoryIcon, color = summary.categoryColor, size = 28)
                Spacer(Modifier.width(8.dp))
                Text(summary.categoryName, style = MaterialTheme.typography.bodyMedium)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(formatCurrencyShort(summary.amount), style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium)
                Text("${"%.1f".format(summary.percentage * 100)}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Spacer(Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = maxWidth,
            modifier = Modifier.fillMaxWidth().height(6.dp),
            color = Color(summary.categoryColor),
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
fun MonthlyTrendItem(summary: MonthlySummary) {
    val maxAmount = maxOf(summary.income, summary.expense, 1.0)
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(summary.month, style = MaterialTheme.typography.bodySmall)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("收 ${formatCurrencyShort(summary.income)}", style = MaterialTheme.typography.labelSmall,
                    color = IncomeGreen)
                Text("支 ${formatCurrencyShort(summary.expense)}", style = MaterialTheme.typography.labelSmall,
                    color = ExpenseRed)
            }
        }
        Spacer(Modifier.height(4.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            LinearProgressIndicator(
                progress = (summary.income / maxAmount).toFloat(),
                modifier = Modifier.weight(1f).height(4.dp),
                color = IncomeGreen,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            Spacer(Modifier.width(4.dp))
            LinearProgressIndicator(
                progress = (summary.expense / maxAmount).toFloat(),
                modifier = Modifier.weight(1f).height(4.dp),
                color = ExpenseRed,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}
