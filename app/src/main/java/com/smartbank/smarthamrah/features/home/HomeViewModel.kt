package com.smartbank.smarthamrah.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbank.smarthamrah.features.customers.chat.domain.model.CheckConversationResult
import com.smartbank.smarthamrah.features.customers.chat.domain.model.ChatProfile
import com.smartbank.smarthamrah.features.customers.chat.domain.usecase.CheckConversationUseCase
import com.smartbank.smarthamrah.features.customers.chat.domain.usecase.GetChatProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val checkConversationUseCase: CheckConversationUseCase,
    private val getChatProfileUseCase: GetChatProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _navigation = Channel<HomeNavigation>()
    val navigation = _navigation.receiveAsFlow()

    init {

    }
    private suspend fun navigateByConversation(result: CheckConversationResult) {
        val bankerName = _uiState.value.bankerProfile.fullName.ifBlank { "بانکدار" }

        if (result.hasActiveConversation && result.categoryId != null) {
            _navigation.send(
                HomeNavigation.Chat(
                    bankerName = bankerName,
                    categoryId = result.categoryId.toString(),
                    categoryTitle = result.categoryTitle ?: "گفتگو با بانکدار"
                )
            )
        } else {
            _navigation.send(HomeNavigation.ChatCategory(bankerName = bankerName))
        }
    }

    fun onChatClicked() {
        if (_uiState.value.isCheckingConversation) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isCheckingConversation = true,
                    errorMessage = null
                )
            }

            val customerId = _uiState.value.customerProfile.customerId

            runCatching {
                checkConversationUseCase()
            }
                .onSuccess { result ->
                    _uiState.update {
                        it.copy(isCheckingConversation = false)
                    }

                    navigateByConversation(result)
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isCheckingConversation = false,
                            errorMessage = throwable.message
                                ?: "خطا در بررسی وضعیت گفتگو"
                        )
                    }
                }
        }
    }



    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

data class HomeUiState(
    val customerProfile: CustomerHomeProfile = CustomerHomeProfile(),
    val bankerProfile: ChatProfile = ChatProfile(),
    val isCheckingConversation: Boolean = false,
    val errorMessage: String? = null
)

sealed interface HomeNavigation {
    data class Chat(
        val bankerName: String,
        val categoryId: String,
        val categoryTitle: String
    ) : HomeNavigation

    data class ChatCategory(
        val bankerName: String
    ) : HomeNavigation

    data object Tickets : HomeNavigation
    data object BankerProfile : HomeNavigation
}
