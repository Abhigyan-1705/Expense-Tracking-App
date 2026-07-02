package com.example.abhigyan.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.abhigyan.data.model.Expense
import com.example.abhigyan.viewmodel.ExpenseViewModel


val categoryColors = listOf(
    Color(0xFFEF5350), // Food - Red
    Color(0xFF42A5F5), // Travel - Blue
    Color(0xFFAB47BC), // Shopping - Purple
    Color(0xFFFFCA28), // Bills - Yellow
    Color(0xFF26C6DA), // Entertainment - Cyan
    Color(0xFF66BB6A), // Education - Green
    Color(0xFFFF7043), // Medical - Orange
    Color(0xFF8D6E63), // Income - Brown
    Color(0xFF78909C)  // Others - Grey
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    navController: NavController,
    viewModel: ExpenseViewModel
) {
    val expenses by viewModel.allExpenses.collectAsState()
    val totalIncome by viewModel.totalIncome.collectAsState()
    val totalExpense by viewModel.totalExpense.collectAsState()
    val balance = (totalIncome ?: 0.0) - (totalExpense ?: 0.0)

    // Category-wise totals (expenses only)
    val categoryTotals = expenses
        .filter { !it.isIncome }
        .groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.amount } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Statistics") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {


            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Financial Summary",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        SummaryItem(
                            label = "Income",
                            amount = totalIncome ?: 0.0,
                            color = Color(0xFF2E7D32)
                        )
                        SummaryItem(
                            label = "Expense",
                            amount = totalExpense ?: 0.0,
                            color = Color(0xFFC62828)
                        )
                        SummaryItem(
                            label = "Balance",
                            amount = balance,
                            color = if (balance >= 0) Color(0xFF2E7D32) else Color(0xFFC62828)
                        )
                    }
                }
            }


            Text(
                "Income vs Expense",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    IncomeExpenseBarChart(
                        income = totalIncome ?: 0.0,
                        expense = totalExpense ?: 0.0
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        LegendItem(color = Color(0xFF2E7D32), label = "Income")
                        LegendItem(color = Color(0xFFC62828), label = "Expense")
                    }
                }
            }


            if (categoryTotals.isNotEmpty()) {
                Text(
                    "Spending by Category",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        PieChart(categoryTotals = categoryTotals)
                        Spacer(modifier = Modifier.height(16.dp))
                        CategoryLegend(categoryTotals = categoryTotals)
                    }
                }
            }


            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CountItem(
                        label = "Total Transactions",
                        count = expenses.size
                    )
                    CountItem(
                        label = "Income Entries",
                        count = expenses.count { it.isIncome }
                    )
                    CountItem(
                        label = "Expense Entries",
                        count = expenses.count { !it.isIncome }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@Composable
fun SummaryItem(label: String, amount: Double, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "₹${"%.0f".format(amount)}",
            style = MaterialTheme.typography.titleSmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun CountItem(label: String, count: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            count.toString(),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@Composable
fun IncomeExpenseBarChart(income: Double, expense: Double) {
    val max = maxOf(income, expense).takeIf { it > 0 } ?: 1.0

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {
        val barWidth = size.width * 0.25f
        val gap = size.width * 0.12f
        val startX = (size.width - (2 * barWidth + gap)) / 2

        // Income bar
        val incomeHeight = ((income / max) * size.height * 0.85f).toFloat()
        drawRect(
            color = Color(0xFF2E7D32),
            topLeft = Offset(startX, size.height - incomeHeight),
            size = Size(barWidth, incomeHeight)
        )

        // Expense bar
        val expenseHeight = ((expense / max) * size.height * 0.85f).toFloat()
        drawRect(
            color = Color(0xFFC62828),
            topLeft = Offset(startX + barWidth + gap, size.height - expenseHeight),
            size = Size(barWidth, expenseHeight)
        )
    }
}


@Composable
fun PieChart(categoryTotals: Map<String, Double>) {
    val total = categoryTotals.values.sum().takeIf { it > 0 } ?: 1.0
    val entries = categoryTotals.entries.toList()

    Canvas(
        modifier = Modifier.size(200.dp)
    ) {
        var startAngle = -90f
        entries.forEachIndexed { index, entry ->
            val sweep = ((entry.value / total) * 360f).toFloat()
            val color = categoryColors[index % categoryColors.size]
            drawArc(
                color = color,
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter = true,
                size = Size(size.width, size.height)
            )
            startAngle += sweep
        }
    }
}


@Composable
fun CategoryLegend(categoryTotals: Map<String, Double>) {
    val entries = categoryTotals.entries.toList()
    val total = categoryTotals.values.sum()

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        entries.forEachIndexed { index, entry ->
            val color = categoryColors[index % categoryColors.size]
            val percent = if (total > 0) (entry.value / total * 100) else 0.0
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(color, RoundedCornerShape(2.dp))
                    )
                    Text(
                        entry.key,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Text(
                    "₹${"%.0f".format(entry.value)} (${"%.1f".format(percent)}%)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


@Composable
fun LegendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, RoundedCornerShape(2.dp))
        )
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}