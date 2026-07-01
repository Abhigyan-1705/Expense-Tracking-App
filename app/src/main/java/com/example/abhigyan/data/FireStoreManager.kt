package com.example.abhigyan.data

import com.example.abhigyan.data.model.Expense
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirestoreManager {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Get current user's expense collection
    private fun userExpensesCollection() =
        db.collection("users")
            .document(auth.currentUser?.uid ?: "unknown")
            .collection("expenses")

    // Upload single expense to Firestore
    suspend fun uploadExpense(expense: Expense) {
        try {
            userExpensesCollection()
                .document(expense.id.toString())
                .set(expense.toMap())
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Delete expense from Firestore
    suspend fun deleteExpense(expenseId: Int) {
        try {
            userExpensesCollection()
                .document(expenseId.toString())
                .delete()
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Fetch all expenses from Firestore
    suspend fun fetchAllExpenses(): List<Expense> {
        return try {
            val snapshot = userExpensesCollection()
                .get()
                .await()
            snapshot.documents.mapNotNull { doc ->
                doc.toExpense()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}

// Extension: Expense → Map for Firestore
fun Expense.toMap(): Map<String, Any> = mapOf(
    "id" to id,
    "title" to title,
    "amount" to amount,
    "category" to category,
    "date" to date,
    "note" to note,
    "isIncome" to isIncome
)

// Extension: Firestore Document → Expense
fun com.google.firebase.firestore.DocumentSnapshot.toExpense(): Expense? {
    return try {
        Expense(
            id = (getLong("id") ?: 0).toInt(),
            title = getString("title") ?: "",
            amount = getDouble("amount") ?: 0.0,
            category = getString("category") ?: "",
            date = getString("date") ?: "",
            note = getString("note") ?: "",
            isIncome = getBoolean("isIncome") ?: false
        )
    } catch (e: Exception) {
        null
    }
}
