package com.example.pfinance.ui.account

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.pfinance.ui.transaction.AccountSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferScreen(navController: NavHostController, viewModel: TransferViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("转账") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    TextButton(onClick = {
                        viewModel.executeTransfer()
                        navController.popBackStack()
                    }) { Text("确认转账", fontWeight = FontWeight.Bold) }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.amount,
                onValueChange = { viewModel.setAmount(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("转账金额") },
                prefix = { Text("¥") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )
            Text("从", style = MaterialTheme.typography.labelLarge)
            AccountSelector(
                accounts = state.accounts,
                selectedAccountId = state.fromAccountId,
                onSelect = { viewModel.setFromAccount(it) }
            )
            Text("到", style = MaterialTheme.typography.labelLarge)
            AccountSelector(
                accounts = state.accounts.filter { it.id != state.fromAccountId },
                selectedAccountId = state.toAccountId,
                onSelect = { viewModel.setToAccount(it) }
            )
            OutlinedTextField(
                value = state.fee,
                onValueChange = { viewModel.setFee(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("手续费（可选）") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )
            OutlinedTextField(
                value = state.note,
                onValueChange = { viewModel.setNote(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("备注") },
                singleLine = true
            )
        }
    }
}
