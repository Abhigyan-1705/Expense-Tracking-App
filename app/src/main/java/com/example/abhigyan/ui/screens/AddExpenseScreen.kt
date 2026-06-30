package com.example.abhigyan.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.abhigyan.data.model.Expense
import com.example.abhigyan.viewmodel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.*

val categories = listOf(
    "🍔 Food", "🚌 Travel", "🛍 Shopping",
    "💡 Bills", "🎬 Entertainment", "📚 Education",
    "🏥 Medical", "💰 Income", "📦 Others"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    navController: NavController,
    viewModel: ExpenseViewModel
) {
    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(categories[0]) }
    var note by remember { mutableStateOf("") }
    var isIncome by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    val today = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        .format(Date())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Transaction") },
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
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Income / Expense Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilterChip(
                    selected = !isIncome,
                    onClick = { isIncome = false },
                    label = { Text("Expense") },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = isIncome,
                    onClick = { isIncome = true },
                    label = { Text("Income") },
                    modifier = Modifier.weight(1f)
                )
            }

            // Title
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                placeholder = { Text("e.g. Lunch, Salary") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Amount
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount (₹)") },
                placeholder = { Text("e.g. 250") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Category Dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                selectedCategory = category
                                expanded = false
                            }
                        )
                    }
                }
            }

            // Date (auto)
            OutlinedTextField(
                value = today,
                onValueChange = {},
                label = { Text("Date") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                enabled = false
            )

            // Note
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Note (optional)") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            // Save Button
            Button(
                onClick = {
                    when {
                        title.isBlank() -> Toast.makeText(
                            context, "Title cannot be empty", Toast.LENGTH_SHORT
                        ).show()
                        amount.isBlank() || amount.toDoubleOrNull() == null -> Toast.makeText(
                            context, "Enter a valid amount", Toast.LENGTH_SHORT
                        ).show()
                        else -> {
                            viewModel.addExpense(
                                Expense(
                                    title = title.trim(),
                                    amount = amount.toDouble(),
                                    category = selectedCategory,
                                    date = today,
                                    note = note.trim(),
                                    isIncome = isIncome
                                )
                            )
                            Toast.makeText(
                                context, "Saved!", Toast.LENGTH_SHORT
                            ).show()
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Save Transaction")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
