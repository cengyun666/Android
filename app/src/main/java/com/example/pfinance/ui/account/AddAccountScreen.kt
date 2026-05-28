package com.example.pfinance.ui.account

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.pfinance.domain.model.AccountType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccountScreen(navController: NavHostController, accountId: Long = -1, viewModel: AddAccountViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(accountId) { viewModel.init(accountId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.isEdit) "编辑账户" else "添加账户") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    TextButton(onClick = {
                        viewModel.save()
                        navController.popBackStack()
                    }) { Text("保存", fontWeight = FontWeight.Bold) }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = { viewModel.setName(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("账户名称") },
                singleLine = true
            )

            // Account type selector
            Text("账户类型", style = MaterialTheme.typography.labelLarge)
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val types = listOf(
                    AccountType.CASH to "现金",
                    AccountType.SAVINGS to "储蓄卡",
                    AccountType.CREDIT_CARD to "信用卡",
                    AccountType.ALIPAY to "支付宝",
                    AccountType.WECHAT to "微信",
                    AccountType.INVESTMENT to "投资"
                )
                items(types.size) { index ->
                    val (type, label) = types[index]
                    FilterChip(
                        selected = state.type == type,
                        onClick = { viewModel.setType(type) },
                        label = { Text(label) }
                    )
                }
            }

            OutlinedTextField(
                value = state.balance,
                onValueChange = { viewModel.setBalance(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("初始余额") },
                prefix = { Text("¥") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )

            if (state.type == AccountType.CREDIT_CARD) {
                OutlinedTextField(
                    value = state.creditLimit,
                    onValueChange = { viewModel.setCreditLimit(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("信用额度") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = state.billDate,
                        onValueChange = { viewModel.setBillDate(it) },
                        modifier = Modifier.weight(1f),
                        label = { Text("账单日") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = state.dueDate,
                        onValueChange = { viewModel.setDueDate(it) },
                        modifier = Modifier.weight(1f),
                        label = { Text("还款日") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
            }

            OutlinedTextField(
                value = state.note,
                onValueChange = { viewModel.setNote(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("备注（可选）") },
                singleLine = true
            )
        }
    }
}

