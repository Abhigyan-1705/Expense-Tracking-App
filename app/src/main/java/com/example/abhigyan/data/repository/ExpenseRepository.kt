package com.example.abhigyan.data.repository

import com.example.abhigyan.data.local.ExpenseDao
import com.example.abhigyan.data.model.Expense
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(private val dao: ExpenseDao) {

    val allExpenses: Flow<List<Expense>> = dao.getAllExpenses()
    val totalExpense: Flow<Double?> = dao.getTotalExpense()
    val totalIncome: Flow<Double?> = dao.getTotalIncome()

    suspend fun insert(expense: Expense) {
        dao.insertExpense(expense)
    }

    suspend fun delete(expense: Expense) {
        dao.deleteExpense(expense)
    }
}
