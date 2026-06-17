package com.smartbank.smarthamrah.features.auth.data.repository

import com.smartbank.smarthamrah.core.preferences.ChatPreferences
import com.smartbank.smarthamrah.features.auth.data.dto.LoginRequestDto
import com.smartbank.smarthamrah.features.auth.data.dto.LogoutRequestDto
import com.smartbank.smarthamrah.features.auth.data.dto.RefreshTokenRequestDto
import com.smartbank.smarthamrah.features.auth.data.dto.VerifyOtpRequestDto
import com.smartbank.smarthamrah.features.auth.data.mapper.toDomain
import com.smartbank.smarthamrah.features.auth.data.remote.AuthApi
import com.smartbank.smarthamrah.features.auth.domain.model.LoginResult
import com.smartbank.smarthamrah.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val preferences: ChatPreferences
) : AuthRepository {

    override suspend fun login(
        username: String,
        password: String
    ): LoginResult {
        return authApi.login(
            LoginRequestDto(
                username = username,
                password = password
            )
        ).toDomain()
    }

    override suspend fun verifyOtp(
        username: String,
        otpCode: String
    ) {
        val tokens = authApi.verifyOtp(
            VerifyOtpRequestDto(
                username = username,
                otpCode = otpCode
            )
        ).toDomain()

        preferences.saveAccessToken(tokens.accessToken)
        preferences.saveRefreshToken(tokens.refreshToken)
        preferences.saveAccessTokenExpiresAt(tokens.accessTokenExpiresAt)
        preferences.saveRefreshTokenExpiresAt(tokens.refreshTokenExpiresAt)
    }

    override suspend fun refreshToken() {
        val currentRefreshToken = preferences.getRefreshToken()
            ?: error("RefreshToken موجود نیست")

        val tokens = authApi.refreshToken(
            RefreshTokenRequestDto(
                refreshToken = currentRefreshToken
            )
        ).toDomain()

        preferences.saveAccessToken(tokens.accessToken)
        preferences.saveRefreshToken(tokens.refreshToken)
        preferences.saveAccessTokenExpiresAt(tokens.accessTokenExpiresAt)
        preferences.saveRefreshTokenExpiresAt(tokens.refreshTokenExpiresAt)
    }

    override suspend fun logout() {
        val refreshToken = preferences.getRefreshToken()

        if (!refreshToken.isNullOrBlank()) {
            runCatching {
                authApi.logout(
                    LogoutRequestDto(
                        refreshToken = refreshToken
                    )
                )
            }
        }

        preferences.clearTokens()
        preferences.saveChatVersion(0L)
    }
}
