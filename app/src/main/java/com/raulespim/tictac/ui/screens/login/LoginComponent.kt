package com.raulespim.tictac.ui.screens.login

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.raulespim.tictac.domain.usecase.GoogleSignInUseCase
import com.raulespim.tictac.ui.screens.login.viewactions.LoginEffect
import com.raulespim.tictac.ui.screens.login.viewactions.LoginIntent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface LoginComponent {
    val state: StateFlow<LoginState>
    val effects: Flow<LoginEffect>
    fun onIntent(intent: LoginIntent)
}

class LoginComponentImpl(
    componentContext: ComponentContext,
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val onNavigateToHome: () -> Unit
) : LoginComponent, ComponentContext by componentContext {

    private val _state = MutableStateFlow(LoginState())
    override val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _effects = Channel<LoginEffect>(Channel.BUFFERED)
    override val effects: Flow<LoginEffect> = _effects.receiveAsFlow()

    private val scope = coroutineScope()

    override fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.OnGoogleSignInClicked -> onGoogleSignInClicked()
        }
    }

    private fun onGoogleSignInClicked() {
        scope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val isSuccess = withContext(Dispatchers.IO) { googleSignInUseCase.invoke() }

                if (isSuccess) {
                    _state.update { it.copy(isLoading = false, isAuthenticated = true) }
                    _effects.send(LoginEffect.OnSignInSuccess)
                    onNavigateToHome()
                } else {
                    _state.update { it.copy(isLoading = false, error = "Login with Google failed") }
                    _effects.send(LoginEffect.OnSignInFailed)
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
                _effects.send(LoginEffect.OnSignInFailed)
            }
        }
    }

}