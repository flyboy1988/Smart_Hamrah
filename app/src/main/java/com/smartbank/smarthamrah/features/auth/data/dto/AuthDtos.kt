package com.smartbank.smarthamrah.features.auth.data.dto

data class LoginRequestDto(
    val username: String,
    val password: String
)

data class LoginResponseDto(
    val requiresOtp: Boolean = false,
    val message: String? = null
)

data class VerifyOtpRequestDto(
    val username: String,
    val otpCode: String
)

data class VerifyOtpResponseDto(
    val accessToken: String? = null,
    val accessTokenExpiresAt: String? = null,
    val refreshToken: String? = null,
    val refreshTokenExpiresAt: String? = null
)

data class RefreshTokenRequestDto(
    val refreshToken: String
)

data class RefreshTokenResponseDto(
    val accessToken: String? = null,
    val accessTokenExpiresAt: String? = null,
    val refreshToken: String? = null,
    val refreshTokenExpiresAt: String? = null
)

data class LogoutRequestDto(
    val refreshToken: String
)

data class LogoutResponseDto(
    val success: Boolean = false,
    val message: String? = null
)
