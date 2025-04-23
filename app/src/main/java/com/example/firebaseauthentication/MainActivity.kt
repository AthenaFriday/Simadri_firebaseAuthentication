package com.example.firebaseauthentication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CustomCredential
import androidx.lifecycle.lifecycleScope
import com.example.firebaseauthentication.data.AuthViewModel
import com.example.firebaseauthentication.ui.NavGraph
import com.example.firebaseauthentication.ui.theme.FirebaseAuthenticationTheme
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val authViewModel = AuthViewModel()
    private lateinit var credentialManager: CredentialManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        credentialManager = CredentialManager.create(baseContext)

        setContent {
            FirebaseAuthenticationTheme {
                NavGraph(
                    authViewModel = authViewModel,
                    onGoogleSignIn = { launchCredentialManager() },
                    onSignOut       = { performSignOut() }
                )
            }
        }
    }

    private fun launchCredentialManager() {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(getString(R.string.default_web_client_id))
            .setFilterByAuthorizedAccounts(true)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            val response = credentialManager.getCredential(this@MainActivity, request)
            handleSignIn(response.credential)
        }

    }

    private fun handleSignIn(credential: Credential) {
        if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            val idToken = GoogleIdTokenCredential
                .createFrom(credential.data)
                .idToken
            authViewModel.signInWithGoogle(idToken)
        }
    }

    private fun performSignOut() {
        authViewModel.signOut()
        lifecycleScope.launch  {
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        }
    }
}
