package com.smartbank.smarthamrah.features.customers.chat.presentation

import com.smartbank.smarthamrah.features.customers.chat.domain.model.ChatCategory
import com.smartbank.smarthamrah.features.customers.chat.domain.model.ChatMessage
import com.smartbank.smarthamrah.features.customers.chat.domain.model.DownloadedFile

data class CustomerChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val categories: List<ChatCategory> = emptyList(),
    val selectedCategoryId: Long? = null,
    val inputText: String = "",
    val conversationId: String? = null,
    val hasMore: Boolean = false,
    val unreadCount: Int = 0,
    val isInitialLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isSending: Boolean = false,
    val isUploading: Boolean = false,
    val errorMessage: String? = null,
    val downloadMessage: String? = null,
    val downloadedFile: DownloadedFile? = null,
    val isDownloadingFile: Boolean = false,
    val selectedRate: Int = 0,
    val isConversationClosed: Boolean = false,
    val showRatingDialog: Boolean = false,
    val pendingRatingConversationId: String? = null,
    val isSubmittingRate: Boolean = false,
    val ratingSubmitted: Boolean = false
)
