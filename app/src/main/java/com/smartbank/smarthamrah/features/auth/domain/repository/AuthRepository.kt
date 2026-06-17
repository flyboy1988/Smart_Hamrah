package com.smartbank.smarthamrah.features.auth.domain.repository

import com.smartbank.smarthamrah.features.auth.domain.model.LoginResult

interface AuthRepository {

    suspend fun login(
        username: String,
        password: String
    ): LoginResult

    suspend fun verifyOtp(
        username: String,
        otpCode: String
    )

    suspend fun refreshToken()

    suspend fun logout()
}
