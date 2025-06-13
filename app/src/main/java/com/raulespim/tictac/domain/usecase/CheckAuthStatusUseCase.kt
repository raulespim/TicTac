package com.raulespim.tictac.domain.usecase

import com.raulespim.tictac.data.repository.AuthRepository

class CheckAuthStatusUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Boolean {
        return authRepository.isUserAuthenticated()
    }
}