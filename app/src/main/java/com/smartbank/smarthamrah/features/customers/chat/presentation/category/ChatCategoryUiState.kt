package com.smartbank.smarthamrah.features.customers.chat.presentation.category


import com.smartbank.smarthamrah.features.customers.chat.domain.model.ChatCategory

data class ChatCategoryUiState(
    val categories: List<ChatCategory> = emptyList(),
    val selectedCategory: ChatCategory? = null,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null
)