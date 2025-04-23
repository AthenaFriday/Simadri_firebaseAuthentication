package com.example.firebaseauthentication

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.example.firebaseauthentication.data.AuthViewModel
import com.example.firebaseauthentication.ui.NavGraph
import com.example.firebaseauthentication.ui.theme.FirebaseAuthenticationTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class MainActivity : ComponentActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private val authViewModel = AuthViewModel()
    private lateinit var googleSignInClient: GoogleSignInClient

    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    account?.idToken?.let { idToken ->
                        Log.d(TAG, "Google ID token: $idToken")
                        authViewModel.signInWithGoogle(idToken)
                    } ?: Log.e(TAG, "Received null ID token")
                } catch (e: ApiException) {
                    Log.e(TAG, "GoogleSignIn failed: ${e.statusCode}", e)
                }
            } else {
                Log.w(TAG, "GoogleSignIn canceled or failed with code ${result.resultCode}")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configure Google Sign-In client
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setContent {
            FirebaseAuthenticationTheme {
                NavGraph(
                    authViewModel = authViewModel,
                    onGoogleSignIn = {
                        Log.d(TAG, "Launching GoogleSignInClient flow")
                        googleSignInLauncher.launch(googleSignInClient.signInIntent)
                    },
                    onSignOut = {
                        Log.d(TAG, "Signing out")
                        authViewModel.signOut()
                    }
                )
            }
        }
    }
}
