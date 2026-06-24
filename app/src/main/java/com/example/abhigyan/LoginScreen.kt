package com.example.abhigyan

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

enum class AuthMode {
    Login, Signup
}

@Composable
fun LoginScreen() {
    var mode by remember { mutableStateOf(AuthMode.Login) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "RupLo",
                    style = MaterialTheme.typography.headlineLarge
                )

                Text(
                    text = if (mode == AuthMode.Login) {
                        "Welcome back"
                    } else {
                        "Create your account"
                    },
                    style = MaterialTheme.typography.titleMedium
                )

                AuthModeToggle(
                    mode = mode,
                    onModeChange = { mode = it }
                )

                AuthForm(
                    mode = mode,
                    onPrimaryClick = { },
                    onSecondaryClick = { }
                )
            }
        }
    }
}

@Composable
private fun AuthModeToggle(
    mode: AuthMode,
    onModeChange: (AuthMode) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (mode == AuthMode.Login) {
            Button(
                onClick = { onModeChange(AuthMode.Login) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Login")
            }
        } else {
            OutlinedButton(
                onClick = { onModeChange(AuthMode.Login) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Login")
            }
        }

        if (mode == AuthMode.Signup) {
            Button(
                onClick = { onModeChange(AuthMode.Signup) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Signup")
            }
        } else {
            OutlinedButton(
                onClick = { onModeChange(AuthMode.Signup) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Signup")
            }
        }
    }
}

@Composable
private fun AuthForm(
    mode: AuthMode,
    onPrimaryClick: () -> Unit,
    onSecondaryClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (mode == AuthMode.Signup) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Full Name") },
                singleLine = true
            )
        }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") },
            singleLine = true
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                TextButton(onClick = { showPassword = !showPassword }) {
                    Text(if (showPassword) "Hide" else "Show")
                }
            }
        )

        if (mode == AuthMode.Signup) {
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Confirm Password") },
                singleLine = true,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation()
            )
        }

        Button(
            onClick = onPrimaryClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text(if (mode == AuthMode.Login) "Login" else "Create Account")
        }

        TextButton(
            onClick = onSecondaryClick,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(
                text = if (mode == AuthMode.Login) {
                    "Don't have an account?"
                } else {
                    "Already have an account?"
                }
            )
        }
    }
}
