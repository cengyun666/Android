package com.example.pfinance.ui.transaction

import androidx.compose.foundation.layout.*
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
import com.example.pfinance.domain.model.Transaction
import com.example.pfinance.domain.model.TransactionType
import com.example.pfinance.ui.components.*
import com.example.pfinance.ui.navigation.Screen
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailScreen(
    navController: NavHostController,
    viewModel: TransactionDetailViewModel = hiltViewModel()
) {
    val transaction by viewModel.transaction.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("交易详情") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        transaction?.let {
                            navController.navigate(Screen.AddTransaction.createRoute(it.type.name, it.id))
                        }
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "编辑")
                    }
                    IconButton(onClick = {
                        transaction?.let { viewModel.deleteTransaction(it) }
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "删除")
                    }
                }
            )
        }
    ) { padding ->
        val tx = transaction ?: return@Scaffold

        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon and category
            CategoryIcon(icon = tx.categoryIcon, color = tx.categoryColor, size = 64)
            Spacer(Modifier.height(12.dp))
            Text(tx.categoryName.ifEmpty { tx.type.name }, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(4.dp))
            AmountText(amount = tx.amount, isExpense = tx.type == TransactionType.EXPENSE,
                showSign = tx.type != TransactionType.TRANSFER)

            Spacer(Modifier.height(24.dp))

            // Details
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    DetailRow("类型", when (tx.type) {
                        TransactionType.EXPENSE -> "支出"
                        TransactionType.INCOME -> "收入"
                        TransactionType.TRANSFER -> "转账"
                        TransactionType.REFUND -> "退款"
                    })
                    DetailRow("账户", tx.accountName)
                    if (tx.type == TransactionType.TRANSFER && tx.toAccountName.isNotEmpty()) {
                        DetailRow("目标账户", tx.toAccountName)
                        if (tx.fee > 0) DetailRow("手续费", formatCurrency(tx.fee))
                    }
                    DetailRow("时间", tx.date.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm")))
                    if (tx.note.isNotEmpty()) DetailRow("备注", tx.note)
                    if (tx.tags.isNotEmpty()) DetailRow("标签", tx.tags.joinToString(", "))
                    if (tx.merchantName.isNotEmpty()) DetailRow("商家", tx.merchantName)
                    if (tx.isReimbursable) DetailRow("报销", tx.reimbursementStatus?.name ?: "待报销")
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium)
    }
}
