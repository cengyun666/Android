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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.pfinance.domain.model.Category
import com.example.pfinance.domain.model.CategoryType
import com.example.pfinance.ui.components.CategoryIcon
import com.example.pfinance.ui.components.SectionHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    navController: NavHostController,
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("分类管理") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding)) {
            item { SectionHeader("支出分类") }
            items(state.expenseCategories) { category ->
                CategoryRow(category)
            }
            item { SectionHeader("收入分类") }
            items(state.incomeCategories) { category ->
                CategoryRow(category)
            }
            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun CategoryRow(category: Category) {
    Surface(Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.surface) {
        Row(Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically) {
            CategoryIcon(icon = category.icon, color = category.color, size = 36)
            Spacer(Modifier.width(12.dp))
            Text(category.name, style = MaterialTheme.typography.bodyLarge, Modifier.weight(1f))
            if (category.isSystem) {
                Text("系统", style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
