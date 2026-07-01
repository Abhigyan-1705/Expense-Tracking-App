package com.example.abhigyan.data.local

import androidx.room.*
import com.example.abhigyan.data.model.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT * FROM expenses ORDER BY id DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT SUM(amount) FROM expenses WHERE isIncome = 0")
    fun getTotalExpense(): Flow<Double?>

    @Query("SELECT SUM(amount) FROM expenses WHERE isIncome = 1")
    fun getTotalIncome(): Flow<Double?>

    @Query("SELECT * FROM expenses")
    suspend fun getAllExpensesOnce(): List<Expense>
}
