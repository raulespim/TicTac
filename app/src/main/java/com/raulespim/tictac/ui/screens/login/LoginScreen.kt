package com.raulespim.tictac.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raulespim.tictac.R
import com.raulespim.tictac.ui.screens.login.viewactions.LoginEffect
import com.raulespim.tictac.ui.screens.login.viewactions.LoginIntent
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(component: LoginComponent) {
    val state by component.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(component) {
        component.effects.collect { effect ->
            when (effect) {
                is LoginEffect.OnSignInSuccess -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            context.getString(R.string.login_successfully)
                        )
                    }
                }
                is LoginEffect.OnSignInFailed -> {
                    coroutineScope.launch { 
                        snackbarHostState.showSnackbar(
                            context.getString(R.string.login_failed, state.error)
                        )
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(onClick = { component.onIntent(LoginIntent.OnGoogleSignInClicked) }) {
                Text(stringResource(R.string.signin_with_google))
            }
        }
        if (state.error != null) {
            Text(text = state.error ?: "", color = androidx.compose.ui.graphics.Color.Red)
        }
    }
}