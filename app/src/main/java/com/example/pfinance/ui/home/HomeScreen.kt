package com.example.pfinance.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.pfinance.domain.model.*
import com.example.pfinance.ui.components.*
import com.example.pfinance.ui.navigation.Screen
import com.example.pfinance.ui.theme.ExpenseRed
import com.example.pfinance.ui.theme.IncomeGreen
import com.example.pfinance.ui.theme.TransferBlue
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(Unit) { viewModel.init(context) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("个人记账", style = MaterialTheme.typography.titleLarge)
                        Text(
                            LocalDate.now().toString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Search.route) }) {
                        Icon(Icons.Default.Search, contentDescription = "搜索")
                    }
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                        Icon(Icons.Default.Settings, contentDescription = "设置")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                // Summary card
                item {
                    SummaryCard(
                        monthlyIncome = state.monthlyIncome,
                        monthlyExpense = state.monthlyExpense,
                        netWorth = state.netWorth
                    )
                }

                // Budget overview
                if (state.budgets.isNotEmpty()) {
                    item { SectionHeader("预算进度") }
                    item {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.budgets) { budget ->
                                BudgetProgressCard(budget) {
                                    navController.navigate(Screen.Budget.route)
                                }
                            }
                        }
                    }
                }

                // Quick actions
                item { SectionHeader("快捷功能") }
                item {
                    QuickActionsRow(
                        onAddExpense = { navController.navigate(Screen.AddTransaction.createRoute("EXPENSE")) },
                        onAddIncome = { navController.navigate(Screen.AddTransaction.createRoute("INCOME")) },
                        onTransfer = { navController.navigate(Screen.Transfer.route) },
                        onAccounts = { navController.navigate(Screen.Accounts.route) }
                    )
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
                        EmptyState(
                            icon = Icons.Default.Receipt,
                            title = "还没有记账记录",
                            subtitle = "点击右下角 + 按钮开始记账"
                        )
                    }
                } else {
                    items(state.recentTransactions) { transaction ->
                        TransactionItem(transaction) {
                            navController.navigate(Screen.TransactionDetail.createRoute(transaction.id))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryCard(monthlyIncome: Double, monthlyExpense: Double, netWorth: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(Modifier.padding(20.dp)) {
            Text("本月收支", style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer)
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("收入", style = MaterialTheme.typography.labelSmall)
                    Text(
                        formatCurrency(monthlyIncome),
                        style = MaterialTheme.typography.titleLarge,
                        color = IncomeGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("支出", style = MaterialTheme.typography.labelSmall)
                    Text(
                        formatCurrency(monthlyExpense),
                        style = MaterialTheme.typography.titleLarge,
                        color = ExpenseRed,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("结余", style = MaterialTheme.typography.labelSmall)
                    Text(
                        formatCurrency(monthlyIncome - monthlyExpense),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (monthlyIncome - monthlyExpense >= 0) IncomeGreen else ExpenseRed
                    )
                }
            }
            if (netWorth != 0.0) {
                Spacer(Modifier.height(12.dp))
                Divider()
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text(
                        "净资产: ${formatCurrency(netWorth)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Composable
fun BudgetProgressCard(budget: Budget, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(budget.name, style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(4.dp))
            Text(
                "${formatCurrencyShort(budget.spent)} / ${formatCurrencyShort(budget.amount)}",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(8.dp))
            ProgressBar(
                progress = budget.progress,
                color = if (budget.isOverBudget) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "${(budget.progress * 100).toInt()}%",
                style = MaterialTheme.typography.labelSmall,
                color = if (budget.isOverBudget) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun QuickActionsRow(
    onAddExpense: () -> Unit,
    onAddIncome: () -> Unit,
    onTransfer: () -> Unit,
    onAccounts: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        QuickActionButton(Icons.Default.RemoveCircleOutline, "记支出", ExpenseRed, onAddExpense)
        QuickActionButton(Icons.Default.AddCircleOutline, "记收入", IncomeGreen, onAddIncome)
        QuickActionButton(Icons.Default.SwapHoriz, "转账", TransferBlue, onTransfer)
        QuickActionButton(Icons.Default.AccountBalance, "账户", MaterialTheme.colorScheme.primary, onAccounts)
    }
}

@Composable
fun QuickActionButton(icon: ImageVector, label: String, color: Color, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable(onClick = onClick).padding(8.dp)) {
        Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(28.dp))
        Spacer(Modifier.height(4.dp))
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun TransactionItem(transaction: Transaction, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryIcon(icon = transaction.categoryIcon, color = transaction.categoryColor)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    transaction.categoryName.ifEmpty { transaction.note.ifEmpty { transaction.type.name } },
                    style = MaterialTheme.typography.bodyLarge
                )
                if (transaction.note.isNotEmpty() && transaction.categoryName.isNotEmpty()) {
                    Text(transaction.note, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text(
                    "${transaction.accountName}  ${formatDateTime(transaction.date)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            AmountText(
                amount = transaction.amount,
                isExpense = transaction.type == TransactionType.EXPENSE,
                showSign = transaction.type != TransactionType.TRANSFER
            )
        }
    }
}
