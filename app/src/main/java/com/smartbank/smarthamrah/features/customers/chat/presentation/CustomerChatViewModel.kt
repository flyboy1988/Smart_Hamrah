package com.smartbank.smarthamrah.features.customers.chat.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbank.smarthamrah.core.chat.ChatEvent
import com.smartbank.smarthamrah.core.chat.ChatSyncManager
import com.smartbank.smarthamrah.features.customers.chat.domain.model.ChatMessage
import com.smartbank.smarthamrah.features.customers.chat.domain.model.MessageType
import com.smartbank.smarthamrah.features.customers.chat.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CustomerChatViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendTextMessageUseCase: SendTextMessageUseCase,
    private val sendFileMessageUseCase: SendFileMessageUseCase,
    private val markChatAsReadUseCase: MarkChatAsReadUseCase,
    private val syncChatUseCase: SyncChatUseCase,
    private val chatSyncManager: ChatSyncManager,
    private val downloadFileUseCase: DownloadFileUseCase,
    private val checkPendingRatingUseCase: CheckPendingRatingUseCase,
    private val rateConversationUseCase: RateConversationUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomerChatUiState())
    val uiState: StateFlow<CustomerChatUiState> = _uiState.asStateFlow()

    private var chatEventJob: Job? = null
    private var currentCustomerId: Long? = null

    fun onScreenStarted(customerId: Long? = null) {
        currentCustomerId = customerId
        observeChatEvents()

        viewModelScope.launch {
            _uiState.update {
                it.copy(isInitialLoading = true, errorMessage = null)
            }

            runCatching {
                val categories = getCategoriesUseCase()
                val sync = syncChatUseCase()

                _uiState.update {
                    it.copy(
                        categories = categories,
                        selectedCategoryId = it.selectedCategoryId ?: categories.firstOrNull()?.id,
                        unreadCount = sync.unreadCount
                    )
                }

                refreshMessages(customerId)
                checkPendingRating()
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(errorMessage = throwable.message ?: "خطا در دریافت اطلاعات چت")
                }
            }

            _uiState.update {
                it.copy(isInitialLoading = false)
            }
        }
    }

    fun onScreenStopped() {
        chatEventJob?.cancel()
        chatEventJob = null
        currentCustomerId = null
    }

    private fun observeChatEvents() {
        if (chatEventJob?.isActive == true) return

        chatEventJob = viewModelScope.launch {
            chatSyncManager.events.collect { event ->
                when (event) {
                    is ChatEvent.DataChanged -> {
                        refreshMessages(currentCustomerId)
                        checkPendingRating()

                    }

                    is ChatEvent.SyncCompleted -> Unit

                    is ChatEvent.Error -> Unit
                }
            }
        }
    }
    private fun checkPendingRating() {
        viewModelScope.launch {
            runCatching {
                checkPendingRatingUseCase()
            }.onSuccess { pending ->

                if (
                    pending.hasPendingRating &&
                    !pending.conversationId.isNullOrBlank()
                ) {
                    _uiState.update {
                        it.copy(
                            isConversationClosed = true,
                            showRatingDialog = true,
                            pendingRatingConversationId = pending.conversationId
                        )
                    }
                }
            }
        }
    }
    fun submitRating(
        rate: Int,
        comment: String?
    ) {
        val conversationId = _uiState.value.pendingRatingConversationId ?: return

        viewModelScope.launch {
            _uiState.update {
                it.copy(isSubmittingRate = true)
            }

            runCatching {
                rateConversationUseCase(
                    conversationId = conversationId,
                    rate = rate,
                    comment = comment
                )
            }.onSuccess {
                _uiState.update {
                    it.copy(
                        isSubmittingRate = false,
                        showRatingDialog = false,
                        pendingRatingConversationId = null,
                        ratingSubmitted = true
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isSubmittingRate = false,
                        errorMessage = throwable.message ?: "ثبت امتیاز ناموفق بود"
                    )
                }
            }
        }
    }

    fun consumeRatingSubmitted() {
        _uiState.update {
            it.copy(ratingSubmitted = false)
        }
    }

    fun onInputChanged(value: String) {
        _uiState.update { it.copy(inputText = value) }
    }

    fun onCategorySelected(categoryId: Long) {
        _uiState.update { it.copy(selectedCategoryId = categoryId) }
    }

    fun sendText(customerId: Long? = null) {
        val state = _uiState.value
        val text = state.inputText.trim()
        val categoryId = state.selectedCategoryId

        if (text.isBlank() || categoryId == null) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(isSending = true, inputText = "", errorMessage = null)
            }

            runCatching {
                sendTextMessageUseCase(
                    customerId = customerId,
                    categoryId = categoryId,
                    text = text,
                    clientMessageId = UUID.randomUUID().toString()
                )

                refreshMessages(customerId)

            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        inputText = text,
                        errorMessage = throwable.message ?: "ارسال پیام ناموفق بود"
                    )
                }
            }

            _uiState.update {
                it.copy(isSending = false)
            }
        }
    }

    fun sendFile(
        uri: Uri,
        mimeType: String? = null,
        customerId: Long? = null
    ) {
        val categoryId = _uiState.value.selectedCategoryId ?: return
        val messageType = mimeType.toMessageType()

        if (messageType == MessageType.UNKNOWN) {
            _uiState.update {
                it.copy(errorMessage = "نوع فایل مجاز نیست")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(isUploading = true, errorMessage = null)
            }

            runCatching {
                sendFileMessageUseCase(
                    customerId = customerId,
                    categoryId = categoryId,
                    uri = uri,
                    messageType = messageType,
                    clientMessageId = UUID.randomUUID().toString()
                )

                refreshMessages(customerId)

            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(errorMessage = throwable.message ?: "ارسال فایل ناموفق بود")
                }
            }

            _uiState.update {
                it.copy(isUploading = false)
            }
        }
    }
    fun downloadFile(fileId: String?) {
        if (fileId.isNullOrBlank()) {
            _uiState.update {
                it.copy(errorMessage = "شناسه فایل موجود نیست")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isDownloadingFile = true,
                    downloadedFile = null,
                    errorMessage = null
                )
            }

            runCatching {
                downloadFileUseCase(fileId)
            }.onSuccess { downloadedFile ->
                _uiState.update {
                    it.copy(
                        isDownloadingFile = false,
                        downloadedFile = downloadedFile
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isDownloadingFile = false,
                        errorMessage = throwable.message ?: "دانلود فایل ناموفق بود"
                    )
                }
            }
        }
    }

    fun clearDownloadedFile() {
        _uiState.update {
            it.copy(downloadedFile = null)
        }
    }
    fun loadOlderMessages(customerId: Long? = null) {
        val state = _uiState.value

        if (!state.hasMore || state.isLoadingMore) return

        val oldestMessageId = state.messages.firstOrNull()?.id ?: return

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoadingMore = true)
            }

            runCatching {
                val categoryId = _uiState.value.selectedCategoryId

                val page = getMessagesUseCase(
                    customerId = customerId,

                    categoryId = categoryId,
                    beforeMessageId = oldestMessageId
                )

                _uiState.update {
                    it.copy(
                        messages = (page.messages + it.messages)
                            .distinctBy { message -> message.id },
                        hasMore = page.hasMore
                    )
                }

            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(errorMessage = throwable.message)
                }
            }

            _uiState.update {
                it.copy(isLoadingMore = false)
            }
        }
    }

    fun clearError() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }

    private suspend fun refreshMessages(customerId: Long?) {
        val categoryId = _uiState.value.selectedCategoryId

        val page = getMessagesUseCase(
            customerId = customerId,
            categoryId = categoryId
        )

        _uiState.update {
            it.copy(
                conversationId = page.conversationId,
                messages = page.messages,
                hasMore = page.hasMore
            )
        }

        markChatAsReadUseCase(page.conversationId)
    }

    private fun String?.toMessageType(): MessageType =
        when (this) {
            "image/jpeg", "image/png" -> MessageType.IMAGE
            "application/pdf" -> MessageType.PDF
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> MessageType.WORD

            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" -> MessageType.WORD

            else -> MessageType.UNKNOWN
        }

    override fun onCleared() {
        onScreenStopped()
        super.onCleared()
    }
}