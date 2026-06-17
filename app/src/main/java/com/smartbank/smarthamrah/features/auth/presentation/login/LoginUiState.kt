package com.smartbank.smarthamrah.features.auth.presentation.login

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val navigateToOtp: Boolean = false
)
