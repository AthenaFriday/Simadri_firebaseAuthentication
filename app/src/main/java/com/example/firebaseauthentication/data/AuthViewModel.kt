package com.example.firebaseauthentication.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Authenticated(val userEmail: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _state = MutableStateFlow<AuthState>(AuthState.Loading)
    val state: StateFlow<AuthState> = _state

    init {
        // Check persisted user:
        val currentUser = auth.currentUser
        _state.value = if (currentUser != null)
            AuthState.Authenticated(currentUser.email ?: "")
        else
            AuthState.Unauthenticated
    }

    fun signUp(email: String, password: String) = viewModelScope.launch {
        _state.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _state.value = AuthState.Authenticated(it.user?.email ?: "")
            }
            .addOnFailureListener {
                _state.value = AuthState.Error(it.localizedMessage ?: "Signup failed")
            }
    }

    fun signIn(email: String, password: String) = viewModelScope.launch {
        _state.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _state.value = AuthState.Authenticated(it.user?.email ?: "")
            }
            .addOnFailureListener {
                _state.value = AuthState.Error(it.localizedMessage ?: "Signin failed")
            }
    }

    fun signOut() {
        auth.signOut()
        _state.value = AuthState.Unauthenticated
    }
}
