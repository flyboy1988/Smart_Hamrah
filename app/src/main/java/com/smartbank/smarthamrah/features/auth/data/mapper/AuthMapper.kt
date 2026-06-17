package com.smartbank.smarthamrah.features.auth.data.mapper

import com.smartbank.smarthamrah.features.auth.data.dto.LoginResponseDto
import com.smartbank.smarthamrah.features.auth.data.dto.RefreshTokenResponseDto
import com.smartbank.smarthamrah.features.auth.data.dto.VerifyOtpResponseDto
import com.smartbank.smarthamrah.features.auth.domain.model.AuthTokens
import com.smartbank.smarthamrah.features.auth.domain.model.LoginResult

fun LoginResponseDto.toDomain(): LoginResult {
    return LoginResult(
        requiresOtp = requiresOtp,
        message = message
    )
}

fun VerifyOtpResponseDto.toDomain(): AuthTokens {
    return AuthTokens(
        accessToken = accessToken ?: error("AccessToken از سرور دریافت نشد"),
        accessTokenExpiresAt = accessTokenExpiresAt,
        refreshToken = refreshToken ?: error("RefreshToken از سرور دریافت نشد"),
        refreshTokenExpiresAt = refreshTokenExpiresAt
    )
}

fun RefreshTokenResponseDto.toDomain(): AuthTokens {
    return AuthTokens(
        accessToken = accessToken ?: error("AccessToken از سرور دریافت نشد"),
        accessTokenExpiresAt = accessTokenExpiresAt,
        refreshToken = refreshToken ?: error("RefreshToken از سرور دریافت نشد"),
        refreshTokenExpiresAt = refreshTokenExpiresAt
    )
}
