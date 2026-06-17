package com.smartbank.smarthamrah.features.auth.presentation.otp

data class OtpUiState(
    val otp: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val navigateToHome: Boolean = false
)
