package com.smartbank.smarthamrah.features.auth.domain.usecase

import com.smartbank.smarthamrah.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class RefreshTokenUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() {
        repository.refreshToken()
    }
}
