package com.raulespim.tictac.ui.screens.login.viewactions

sealed class LoginIntent {
    data object OnGoogleSignInClicked : LoginIntent()

}

sealed class LoginEffect {
    data object OnSignInSuccess : LoginEffect()
    data object OnSignInFailed : LoginEffect()
}