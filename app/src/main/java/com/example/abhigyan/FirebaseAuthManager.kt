package com.example.abhigyan

import com.google.firebase.auth.FirebaseAuth

object FirebaseAuthManager {

    private val auth = FirebaseAuth.getInstance()

    fun signup(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it.message ?: "Signup failed")
            }
    }

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it.message ?: "Login failed")
            }
    }

    fun logout() {
        auth.signOut()
    }

    fun currentUser() = auth.currentUser
}