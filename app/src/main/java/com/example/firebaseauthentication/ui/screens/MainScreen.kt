package com.example.firebaseauthentication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    userEmail: String,
    onSignOut: () -> Unit,
    onCrash: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome, $userEmail", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(24.dp))

        Button(onClick = onCrash, modifier = Modifier.fillMaxWidth()) {
            Text("Crash App")
        }
        Spacer(Modifier.height(16.dp))

        Button(onClick = onSignOut, modifier = Modifier.fillMaxWidth()) {
            Text("Sign Out")
        }
    }
}
