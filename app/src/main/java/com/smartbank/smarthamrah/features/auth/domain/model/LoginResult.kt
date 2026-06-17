package com.smartbank.smarthamrah.features.auth.domain.model

data class LoginResult(
    val requiresOtp: Boolean,
    val message: String?
)
