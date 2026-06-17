package com.smartbank.smarthamrah.features.auth.presentation.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbank.smarthamrah.core.chat.ChatSyncManager
import com.smartbank.smarthamrah.core.network.toReadableMessage
import com.smartbank.smarthamrah.features.auth.domain.usecase.LoginUseCase
import com.smartbank.smarthamrah.features.auth.domain.usecase.VerifyOtpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.content.Context
import com.smartbank.smarthamrah.core.utils.BankerProfilePreferenceManager
import com.smartbank.smarthamrah.features.profile.domain.usecase.GetBankerProfileUseCase
import dagger.hilt.android.qualifiers.ApplicationContext

@HiltViewModel
class OtpViewModel @Inject constructor(
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val loginUseCase: LoginUseCase,
    private val chatSyncManager: ChatSyncManager,
    @ApplicationContext private val context: Context,
    private val getBankerProfileUseCase: GetBankerProfileUseCase
)  : ViewModel() {

    private val _uiState = MutableStateFlow(OtpUiState())
    val uiState: StateFlow<OtpUiState> = _uiState.asStateFlow()

    fun onOtpChanged(value: String) {
        _uiState.update {
            it.copy(
                otp = value.filter { char -> char.isDigit() }.take(6),
                errorMessage = null
            )
        }
    }

    fun verifyOtp(username: String) {
        val state = _uiState.value
        if (state.isLoading) return

        val otp = state.otp

        if (otp.length < 6) {
            _uiState.update {
                it.copy(errorMessage = "کد تایید را کامل وارد کنید")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            runCatching {
                verifyOtpUseCase(
                    username = username,
                    otpCode = otp
                )

            }.onSuccess {

                runCatching {
                    getBankerProfileUseCase()
                }.onSuccess { profile ->
                    BankerProfilePreferenceManager.saveProfile(
                        context = context,
                        profile = profile
                    )
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        navigateToHome = true
                    )
                }
                chatSyncManager.resetSession()
                chatSyncManager.start()
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

    fun resendOtp(
        username: String,
        password: String
    ) {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            runCatching {
                loginUseCase(
                    username = username,
                    password = password
                )
            }.onSuccess {
                _uiState.update {
                    it.copy(
                        otp = "",
                        isLoading = false,
                        errorMessage = null
                    )
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
            it.copy(navigateToHome = false)
        }
    }
}
