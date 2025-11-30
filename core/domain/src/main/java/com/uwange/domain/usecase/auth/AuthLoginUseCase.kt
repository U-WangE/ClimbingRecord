package com.uwange.domain.usecase.auth

import com.uwange.domain.model.auth.OAuthProvider
import com.uwange.domain.model.user.UserRole
import com.uwange.domain.repository.AuthRepository
import javax.inject.Inject

class AuthLoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): UserRole? {
        return if (authRepository.isLoggedIn()) {
            authRepository.getCurrentUser()?.userRole
        } else {
            null
        }
    }
}