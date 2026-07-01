package com.example.abhigyan.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.abhigyan.data.local.ExpenseDao
import com.example.abhigyan.data.model.Expense
import com.example.abhigyan.data.remote.FirestoreManager
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(
    private val dao: ExpenseDao,
    private val context: Context
) {

    val allExpenses: Flow<List<Expense>> = dao.getAllExpenses()
    val totalExpense: Flow<Double?> = dao.getTotalExpense()
    val totalIncome: Flow<Double?> = dao.getTotalIncome()

    suspend fun insert(expense: Expense) {
        // Always save to Room first (offline-first)
        dao.insertExpense(expense)

        // If online, sync to Firestore
        if (isOnline()) {
            FirestoreManager.uploadExpense(expense)
        }
    }

    suspend fun delete(expense: Expense) {
        // Always delete from Room first
        dao.deleteExpense(expense)

        // If online, delete from Firestore too
        if (isOnline()) {
            FirestoreManager.deleteExpense(expense.id)
        }
    }

    // Sync all local Room data to Firestore
    suspend fun syncToFirestore() {
        if (!isOnline()) return
        try {
            val localExpenses = dao.getAllExpensesOnce()
            localExpenses.forEach { expense ->
                FirestoreManager.uploadExpense(expense)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Check internet connectivity
    private fun isOnline(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}