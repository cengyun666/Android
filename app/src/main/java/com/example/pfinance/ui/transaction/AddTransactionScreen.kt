package com.example.pfinance.ui.transaction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.pfinance.domain.model.*
import com.example.pfinance.ui.components.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    navController: NavHostController,
    type: String,
    editId: Long,
    viewModel: AddTransactionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(type, editId) { viewModel.init(type, editId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (editId > 0) "编辑记录" else "记一笔",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Close, contentDescription = "关闭")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            viewModel.saveTransaction()
                            navController.popBackStack()
                        }
                    ) { Text("保存", fontWeight = FontWeight.Bold) }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Type selector
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    FilterChip(
                        selected = state.transactionType == "EXPENSE",
                        onClick = { viewModel.setType("EXPENSE") },
                        label = { Text("支出") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ExpenseRed.copy(alpha = 0.15f),
                            selectedLabelColor = ExpenseRed
                        )
                    )
                    Spacer(Modifier.width(12.dp))
                    FilterChip(
                        selected = state.transactionType == "INCOME",
                        onClick = { viewModel.setType("INCOME") },
                        label = { Text("收入") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = IncomeGreen.copy(alpha = 0.15f),
                            selectedLabelColor = IncomeGreen
                        )
                    )
                    Spacer(Modifier.width(12.dp))
                    FilterChip(
                        selected = state.transactionType == "TRANSFER",
                        onClick = { viewModel.setType("TRANSFER") },
                        label = { Text("转账") }
                    )
                }
            }

            // Amount input
            item {
                OutlinedTextField(
                    value = state.amount,
                    onValueChange = { viewModel.setAmount(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("金额") },
                    prefix = { Text("¥", style = MaterialTheme.typography.headlineMedium) },
                    textStyle = MaterialTheme.typography.headlineMedium,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    placeholder = { Text("0.00") }
                )
            }

            // Category selector
            item {
                Text("分类", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.categories) { category ->
                        CategoryChip(
                            category = category,
                            selected = category.id == state.selectedCategoryId,
                            onClick = { viewModel.selectCategory(category.id) }
                        )
                    }
                }
            }

            // Account selector
            item {
                Text("账户", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(8.dp))
                AccountSelector(
                    accounts = state.accounts,
                    selectedAccountId = state.selectedAccountId,
                    onSelect = { viewModel.selectAccount(it) }
                )
            }

            // To-account for transfers
            if (state.transactionType == "TRANSFER") {
                item {
                    Text("目标账户", style = MaterialTheme.typography.labelLarge)
                    Spacer(Modifier.height(8.dp))
                    AccountSelector(
                        accounts = state.accounts.filter { it.id != state.selectedAccountId },
                        selectedAccountId = state.selectedToAccountId,
                        onSelect = { viewModel.selectToAccount(it) }
                    )
                }
                item {
                    OutlinedTextField(
                        value = state.fee,
                        onValueChange = { viewModel.setFee(it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("手续费（可选）") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true
                    )
                }
            }

            // Date picker
            item {
                val dateStr = state.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                OutlinedTextField(
                    value = dateStr,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth().clickable { viewModel.showDatePicker() },
                    readOnly = true,
                    label = { Text("时间") },
                    trailingIcon = { Icon(Icons.Default.CalendarToday, null) }
                )
            }

            // Note
            item {
                OutlinedTextField(
                    value = state.note,
                    onValueChange = { viewModel.setNote(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("备注") },
                    singleLine = true
                )
            }

            // Tags
            item {
                OutlinedTextField(
                    value = state.tags,
                    onValueChange = { viewModel.setTags(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("标签（逗号分隔）") },
                    singleLine = true
                )
            }

            // Reimbursable toggle
            if (state.transactionType == "EXPENSE") {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = state.isReimbursable,
                            onCheckedChange = { viewModel.setReimbursable(it) }
                        )
                        Text("可报销")
                    }
                }
            }

            // Merchant name
            item {
                OutlinedTextField(
                    value = state.merchantName,
                    onValueChange = { viewModel.setMerchantName(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("商家名称（可选）") },
                    singleLine = true
                )
            }
        }
    }

    if (state.showDatePickerDialog) {
        // In production, use DatePickerDialog
        viewModel.hideDatePicker()
    }
}

@Composable
fun CategoryChip(category: Category, selected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        Surface(
            modifier = Modifier.size(48.dp),
            shape = MaterialTheme.shapes.small,
            color = if (selected) Color(category.color).copy(alpha = 0.2f)
            else MaterialTheme.colorScheme.surfaceVariant
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = iconNameToVector(category.icon),
                    contentDescription = category.name,
                    tint = if (selected) Color(category.color) else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Text(
            category.name,
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun AccountSelector(accounts: List<Account>, selectedAccountId: Long?, onSelect: (Long) -> Unit) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(accounts) { account ->
            FilterChip(
                selected = account.id == selectedAccountId,
                onClick = { onSelect(account.id) },
                label = { Text(account.name) },
                leadingIcon = {
                    Icon(
                        iconNameToVector(account.icon),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            )
        }
    }
}
