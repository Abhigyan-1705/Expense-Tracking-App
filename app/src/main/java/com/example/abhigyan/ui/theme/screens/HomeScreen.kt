package com.example.abhigyan.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.abhigyan.ui.theme.navigation.Screen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(navController: NavController) {
    val user = FirebaseAuth.getInstance().currentUser

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "Welcome",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = user?.email ?: ""
        )
        Spacer(modifier = Modifier.height(30.dp))
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text("Current Balance")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "₹0",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = { /* Add Expense */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Expense")
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Home.route) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }
    }
}
