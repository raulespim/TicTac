package com.raulespim.tictac.data.repository

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.BuildConfig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.raulespim.tictac.R
import kotlinx.coroutines.tasks.await

interface AuthRepository {
    suspend fun performGoogleSignIn(): Boolean
    fun isUserAuthenticated(): Boolean
    fun getCurrentUserId(): String?
}

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val context: Context
) : AuthRepository {

     val credentialManager = CredentialManager.create(context)

    override suspend fun performGoogleSignIn(): Boolean {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(context.getString(R.string.web_client_id))
            .setFilterByAuthorizedAccounts(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        return try {
            val result = credentialManager.getCredential(context, request)
            when (val credential = result.credential) {
                is CustomCredential -> {
                    if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        val idToken = googleIdTokenCredential.idToken
                        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                        firebaseAuth.signInWithCredential(firebaseCredential).await()
                        true
                    } else {
                        false
                    }
                }
                else -> false
            }
        } catch (e: GetCredentialException) {
            false
        } catch (e: Exception) {
            false
        }
    }

    override fun isUserAuthenticated(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }
}