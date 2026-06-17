package com.smartbank.smarthamrah.features.auth.domain.usecase

import com.smartbank.smarthamrah.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class VerifyOtpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        username: String,
        otpCode: String
    ) {
        repository.verifyOtp(
            username = username,
            otpCode = otpCode
        )
    }
}
