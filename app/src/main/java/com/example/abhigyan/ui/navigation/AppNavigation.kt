package com.example.abhigyan.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.abhigyan.ui.screens.AddExpenseScreen
import com.example.abhigyan.ui.screens.HomeScreen
import com.example.abhigyan.ui.screens.LoginScreen
import com.example.abhigyan.ui.screens.SplashScreen
import com.example.abhigyan.viewmodel.ExpenseViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: ExpenseViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController, viewModel)
        }
        composable(Screen.AddExpense.route) {
            AddExpenseScreen(navController, viewModel)
        }
        composable(Screen.Statistics.route) {
            StatisticsScreen(navController, viewModel)
        }
    }
}
