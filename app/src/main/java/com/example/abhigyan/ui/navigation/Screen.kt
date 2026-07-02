package com.example.abhigyan.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Home : Screen("home")
    object AddExpense : Screen("add_expense")

    object Statistics : Screen("statistics")
}
