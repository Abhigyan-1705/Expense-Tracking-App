package com.example.abhigyan

import android.app.Application
import com.example.abhigyan.data.local.ExpenseDatabase

class RupLoApp : Application() {
    val database: ExpenseDatabase by lazy {
        ExpenseDatabase.getDatabase(this)
    }
}
