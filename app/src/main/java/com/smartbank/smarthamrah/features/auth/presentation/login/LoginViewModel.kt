package com.smartbank.smarthamrah.features.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbank.smarthamrah.core.network.toReadableMessage
import com.smartbank.smarthamrah.features.auth.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onUsernameChanged(value: String) {
        _uiState.update {
            it.copy(username = value, errorMessage = null)
        }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update {
            it.copy(password = value, errorMessage = null)
        }
    }

    fun togglePasswordVisibility() {
        _uiState.update {
            it.copy(isPasswordVisible = !it.isPasswordVisible)
        }
    }

    fun login() {
        val state = _uiState.value
        if (state.isLoading) return

        val username = state.username.trim()
        val password = state.password

        if (username.isBlank()) {
            _uiState.update { it.copy(errorMessage = "نام کاربری را وارد کنید") }
            return
        }

        if (password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "رمز عبور را وارد کنید") }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            runCatching {
                loginUseCase(username, password)
            }.onSuccess { result ->
                if (result.requiresOtp) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            navigateToOtp = true
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message ?: "ورود تایید شد اما OTP درخواست نشد"
                        )
                    }
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = throwable.toReadableMessage()
                    )
                }
            }
        }
    }

    fun consumeNavigation() {
        _uiState.update {
            it.copy(navigateToOtp = false)
        }
    }
}
