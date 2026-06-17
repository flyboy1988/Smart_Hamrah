package com.smartbank.smarthamrah.features.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbank.smarthamrah.core.chat.ChatSyncManager
import com.smartbank.smarthamrah.core.network.toReadableMessage
import com.smartbank.smarthamrah.core.utils.BankerProfilePreferenceManager
import com.smartbank.smarthamrah.features.auth.domain.usecase.LogoutUseCase
import com.smartbank.smarthamrah.features.profile.domain.usecase.GetBankerProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val chatSyncManager: ChatSyncManager,
    private val getBankerProfileUseCase: GetBankerProfileUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileScreenState())
    val uiState = _uiState.asStateFlow()

    private val _navigation = MutableSharedFlow<ProfileNavigation>()
    val navigation = _navigation.asSharedFlow()

    init {
        loadProfileFromPreference()
        refreshProfile()
    }

    private fun loadProfileFromPreference() {
        val profile = BankerProfilePreferenceManager.getProfile(context)

        _uiState.update {
            it.copy(
                profile = ProfileUiState(
                    fullName = profile.fullName.ifBlank { "بانکدار" },
                    personnelCode = profile.personalNumber.ifBlank { "-" },
                    mobile = profile.mobileNumber.ifBlank { "-" },
                    position = profile.organizationPosition.ifBlank { "-" },
                    province = profile.activityProvinceTitle.ifBlank { "-" },
                    city = profile.activityCityTitle.ifBlank { "-" },
                    branch = profile.branchName.ifBlank { "-" },
                    score = 0f,
                    voteCount = 0
                )
            )
        }
    }

    fun refreshProfile() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isProfileLoading = true,
                    errorMessage = null
                )
            }

            runCatching {
                getBankerProfileUseCase()
            }.onSuccess { profile ->

                BankerProfilePreferenceManager.saveProfile(
                    context = context,
                    profile = profile
                )

                _uiState.update {
                    it.copy(
                        isProfileLoading = false,
                        profile = ProfileUiState(
                            fullName = profile.fullName.ifBlank { "بانکدار" },
                            personnelCode = profile.personalNumber.ifBlank { "-" },
                            mobile = profile.mobileNumber.ifBlank { "-" },
                            position = profile.organizationPosition.ifBlank { "-" },
                            province = profile.activityProvinceTitle.ifBlank { "-" },
                            city = profile.activityCityTitle.ifBlank { "-" },
                            branch = profile.branchName.ifBlank { "-" },
                            score = it.profile.score,
                            voteCount = it.profile.voteCount
                        )
                    )
                }

            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isProfileLoading = false,
                        errorMessage = throwable.toReadableMessage()
                    )
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLogoutLoading = true,
                    errorMessage = null
                )
            }

            runCatching {
                chatSyncManager.stop()
                BankerProfilePreferenceManager.clearProfile(context)
                logoutUseCase()
            }.onSuccess {
                _uiState.update {
                    it.copy(isLogoutLoading = false)
                }

                _navigation.emit(ProfileNavigation.Login)
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLogoutLoading = false,
                        errorMessage = throwable.toReadableMessage()
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }
}

data class ProfileScreenState(
    val isProfileLoading: Boolean = false,
    val isLogoutLoading: Boolean = false,
    val errorMessage: String? = null,
    val profile: ProfileUiState = ProfileUiState(
        fullName = "بانکدار",
        personnelCode = "-",
        mobile = "-",
        position = "-",
        province = "-",
        city = "-",
        branch = "-",
        score = 0f,
        voteCount = 0
    )
)

sealed interface ProfileNavigation {
    data object Login : ProfileNavigation
}