package com.example.firebaseauthentication.ui

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.firebaseauthentication.data.AuthState
import com.example.firebaseauthentication.data.AuthViewModel
import com.example.firebaseauthentication.ui.screens.LoginScreen
import com.example.firebaseauthentication.ui.screens.MainScreen
import com.example.firebaseauthentication.ui.screens.SignUpScreen

@Composable
fun NavGraph(
    authViewModel: AuthViewModel,
    onGoogleSignIn: () -> Unit,
    onSignOut: () -> Unit
) {
    val navController = rememberNavController()

    // Observe auth state lifecycle-aware
    val lifecycleOwner = LocalLifecycleOwner.current
    val stateFlow = remember(authViewModel.state, lifecycleOwner) {
        authViewModel.state.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
    val state by stateFlow.collectAsState(initial = AuthState.Loading)

    // Imperative navigation when state changes
    LaunchedEffect(state) {
        when (state) {
            is AuthState.Authenticated -> {
                navController.navigate("main") {
                    popUpTo("login") { inclusive = true }
                }
            }
            is AuthState.Unauthenticated -> {
                navController.navigate("login") {
                    popUpTo("main") { inclusive = true }
                }
            }
            else -> { /* Loading or Error: remain */ }
        }
    }

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                authState = state,
                onSignIn = { email, pw -> authViewModel.signIn(email, pw) },
                onGoToSignUp = { navController.navigate("signup") },
                onGoogleSignIn = onGoogleSignIn
            )
        }
        composable("signup") {
            SignUpScreen(
                authState = state,
                onSignUp = { email, pw -> authViewModel.signUp(email, pw) },
                onBack = { navController.popBackStack() }
            )
        }
        composable("main") {
            MainScreen(
                userEmail = (state as? AuthState.Authenticated)?.userEmail.orEmpty(),
                onSignOut = onSignOut,
                onCrash = { throw RuntimeException("App crashed") }
            )
        }
    }
}
