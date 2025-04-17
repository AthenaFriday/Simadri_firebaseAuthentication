package com.example.firebaseauthentication.ui

import LoginScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.*
import com.example.firebaseauthentication.data.AuthState
import com.example.firebaseauthentication.data.AuthViewModel
import com.example.firebaseauthentication.ui.screens.MainScreen
import com.example.firebaseauthentication.ui.screens.SignUpScreen

@Composable
fun NavGraph(authViewModel: AuthViewModel) {
    val nav = rememberNavController()
    val state = authViewModel.state.collectAsState().value

    NavHost(
        navController = nav,
        startDestination = when (state) {
            is AuthState.Authenticated -> "main"
            else -> "login"
        }
    ) {
        composable("login") {
            LoginScreen(
                authState = state,
                onSignIn = { email, pw -> authViewModel.signIn(email, pw) },
                onGoToSignUp = { nav.navigate("signup") }
            )
        }
        composable("signup") {
            SignUpScreen(
                authState = state,
                onSignUp = { email, pw -> authViewModel.signUp(email, pw) },
                onBack = { nav.popBackStack() }
            )
        }
        composable("main") {
            MainScreen(
                userEmail = (state as? AuthState.Authenticated)?.userEmail.orEmpty(),
                onSignOut = { authViewModel.signOut(); nav.navigate("login") },
                onCrash = { throw RuntimeException("App got crashed") }
            )
        }
    }
}
