package com.example.pfinance.ui.settings

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.pfinance.domain.model.SavingGoal
import com.example.pfinance.ui.components.EmptyState
import com.example.pfinance.ui.components.ProgressBar
import com.example.pfinance.ui.components.formatCurrencyShort

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingGoalsScreen(
    navController: NavHostController,
    viewModel: SavingGoalsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("存钱目标") },
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
        if (state.goals.isEmpty()) {
            Column(Modifier.fillMaxSize().padding(padding)) {
                EmptyState(Icons.Default.Flag, "还没有存钱目标", "设置目标帮助你更好地攒钱")
            }
        } else {
            LazyColumn(
                Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.goals) { goal ->
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp)) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(goal.name, style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold)
                                Icon(Icons.Default.Flag, null, tint = Color(goal.color))
                            }
                            Spacer(Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()) {
                                Text(formatCurrencyShort(goal.currentAmount),
                                    style = MaterialTheme.typography.titleMedium)
                                Text("目标: ${formatCurrencyShort(goal.targetAmount)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Spacer(Modifier.height(8.dp))
                            ProgressBar(progress = goal.progress, color = Color(goal.color))
                            Spacer(Modifier.height(4.dp))
                            Text("${(goal.progress * 100).toInt()}% 完成",
                                style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }
    }
}
