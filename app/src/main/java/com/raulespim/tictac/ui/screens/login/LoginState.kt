package com.raulespim.tictac.ui.screens.login

data class LoginState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val error: String? = null
)