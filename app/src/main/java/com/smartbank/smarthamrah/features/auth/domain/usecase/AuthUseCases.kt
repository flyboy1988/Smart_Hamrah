package com.smartbank.smarthamrah.features.auth.domain.usecase

import com.smartbank.smarthamrah.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class SendOtpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        username: String,
        password: String
    ) = repository.login(username, password)
}

class AuthenticateUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        username: String,
        otp: String
    ) = repository.verifyOtp(username, otp)
}

