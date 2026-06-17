package com.smartbank.smarthamrah.features.customers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbank.smarthamrah.features.customers.chat.domain.usecase.CheckConversationUseCase
import com.smartbank.smarthamrah.features.customers.data.dto.BankerCustomersRequestDto
import com.smartbank.smarthamrah.features.customers.domain.model.BankerCustomer
import com.smartbank.smarthamrah.features.customers.domain.usecase.GetBankerCustomersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CustomersUiState(
    val isLoading: Boolean = false,
    val isCheckingConversation: Boolean = false,
    val errorMessage: String? = null,
    val customers: List<BankerCustomer> = emptyList(),
    val searchQuery: String = ""
)

sealed interface CustomersNavigation {
    data class Chat(
        val customerName: String,
        val categoryId: String,
        val categoryTitle: String,
        val customerId: Long
    ) : CustomersNavigation

    data class ChatCategory(
        val customerName: String,
        val customerId: Long
    ) : CustomersNavigation
}

@HiltViewModel
class CustomersViewModel @Inject constructor(
    private val getBankerCustomersUseCase: GetBankerCustomersUseCase,
    private val checkConversationUseCase: CheckConversationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomersUiState())
    val uiState: StateFlow<CustomersUiState> = _uiState.asStateFlow()

    private val _navigation = Channel<CustomersNavigation>()
    val navigation = _navigation.receiveAsFlow()

    init {
        loadCustomers()
    }

    fun onSearchQueryChanged(value: String) {
        _uiState.update {
            it.copy(searchQuery = value)
        }
    }

    fun searchCustomers() {
        loadCustomers(
            customerName = _uiState.value.searchQuery
                .trim()
                .takeIf { it.isNotBlank() }
        )
    }

    fun loadCustomers(customerName: String? = null) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            runCatching {
                getBankerCustomersUseCase(
                    BankerCustomersRequestDto(customerName)
                )
            }.onSuccess { customers ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        customers = customers
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "خطا در دریافت لیست مشتریان"
                    )
                }
            }
        }
    }

    fun onMessageClick(customer: BankerCustomer) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isCheckingConversation = true,
                    errorMessage = null
                )
            }

            runCatching {
                checkConversationUseCase(customer.customerId)
            }.onSuccess { result ->

                val customerName = customer.fullName.ifBlank { "مشتری" }

                if (result.hasConversation && result.hasActiveConversation) {
                    _navigation.send(
                        CustomersNavigation.Chat(
                            customerName = customerName,
                            categoryId = result.categoryId.orEmpty(),
                            categoryTitle = result.categoryTitle ?: "چت مشتری",
                            customerId = customer.customerId
                        )
                    )
                } else {
                    _navigation.send(
                        CustomersNavigation.ChatCategory(
                            customerName = customerName,
                            customerId = customer.customerId
                        )
                    )
                }

                _uiState.update {
                    it.copy(isCheckingConversation = false)
                }

            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isCheckingConversation = false,
                        errorMessage = throwable.message ?: "خطا در بررسی وضعیت گفتگو"
                    )
                }
            }
        }
    }
}