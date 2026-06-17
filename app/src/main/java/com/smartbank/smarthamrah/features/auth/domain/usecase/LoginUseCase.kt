package com.smartbank.smarthamrah.features.auth.domain.usecase

import com.smartbank.smarthamrah.features.auth.domain.model.LoginResult
import com.smartbank.smarthamrah.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        username: String,
        password: String
    ): LoginResult {
        return repository.login(
            username = username,
            password = password
        )
    }
}
