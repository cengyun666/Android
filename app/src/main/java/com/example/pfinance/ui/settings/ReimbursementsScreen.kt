package com.example.pfinance.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pfinance.ui.components.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReimbursementsScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("报销管理") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            EmptyState(
                icon = Icons.Default.ReceiptLong,
                title = "暂无待报销记录",
                subtitle = "记账时标记「可报销」即可在此追踪报销状态"
            )
        }
    }
}
