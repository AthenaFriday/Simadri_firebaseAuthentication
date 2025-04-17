package com.example.firebaseauthentication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.firebaseauthentication.data.AuthViewModel
import com.example.firebaseauthentication.ui.NavGraph
import com.example.firebaseauthentication.ui.theme.FirebaseAuthenticationTheme

class MainActivity : ComponentActivity() {
    private val authViewModel = AuthViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseAuthenticationTheme {
                NavGraph(authViewModel)
            }
        }
    }
}
