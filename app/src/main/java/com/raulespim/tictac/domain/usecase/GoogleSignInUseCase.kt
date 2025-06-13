package com.raulespim.tictac.domain.usecase

import com.raulespim.tictac.data.repository.AuthRepository

class GoogleSignInUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Boolean {
        return authRepository.performGoogleSignIn()
    }
}