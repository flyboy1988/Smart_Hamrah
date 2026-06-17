package com.smartbank.smarthamrah.features.auth.domain.model

data class AuthTokens(
    val accessToken: String,
    val accessTokenExpiresAt: String?,
    val refreshToken: String,
    val refreshTokenExpiresAt: String?
)
