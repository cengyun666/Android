package com.example.pfinance.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pfinance.ui.components.SectionHeader
import com.example.pfinance.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("设置", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            item {
                SectionHeader("数据管理")
                SettingsMenuItem(Icons.Default.Backup, "备份与恢复") { navController.navigate(Screen.BackupRestore.route) }
                SettingsMenuItem(Icons.Default.FileUpload, "数据导入") { navController.navigate(Screen.DataImport.route) }
                SettingsMenuItem(Icons.Default.FileDownload, "数据导出") { navController.navigate(Screen.DataExport.route) }
            }

            item {
                SectionHeader("通用")
                SettingsMenuItem(Icons.Default.Delete, "回收站") { }
                SettingsMenuItem(Icons.Default.Lock, "应用锁") { navController.navigate(Screen.AppLock.route) }
                SettingsMenuItem(Icons.Default.Palette, "主题设置") { }
                SettingsMenuItem(Icons.Default.Language, "语言") { }
            }

            item {
                SectionHeader("分类管理")
                SettingsMenuItem(Icons.Default.Category, "收支分类") { navController.navigate(Screen.Categories.route) }
            }

            item {
                SectionHeader("周期与提醒")
                SettingsMenuItem(Icons.Default.Loop, "周期账单") { navController.navigate(Screen.RecurringTransactions.route) }
            }

            item {
                SectionHeader("其他")
                SettingsMenuItem(Icons.Default.Flag, "存钱目标") { navController.navigate(Screen.SavingGoals.route) }
                SettingsMenuItem(Icons.Default.ReceiptLong, "报销管理") { navController.navigate(Screen.Reimbursements.route) }
                SettingsMenuItem(Icons.Default.Group, "共享账本") { navController.navigate(Screen.SharedLedgers.route) }
            }

            item {
                Spacer(Modifier.height(32.dp))
                Text(
                    "个人记账 v1.0.0",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun SettingsMenuItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(16.dp))
            Text(title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
        }
    }
    Divider(modifier = Modifier.padding(start = 54.dp))
}
