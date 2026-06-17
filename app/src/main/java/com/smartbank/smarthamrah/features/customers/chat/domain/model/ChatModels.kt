package com.smartbank.smarthamrah.features.customers.chat.domain.model

import android.net.Uri

data class ChatCategory(
    val id: Long,
    val title: String
)

data class ChatMessage(
    val id: String,
    val conversationId: String?,
    val senderId: Long,
    val senderType: SenderType,
    val senderName: String,
    val messageType: MessageType,
    val text: String?,
    val file: ChatFile?,
    val deliveryStatus: DeliveryStatus,
    val createdAtUtc: String,
    val categoryId: Long,
    val categoryTitle: String,
    val conversationStatusId: Int,
    val conversationStatusTitle: String
)

data class ChatFile(
    val fileId: String?,
    val fileName: String?,
    val fileSize: Long?,
    val mimeType: String?,
    val downloadUrl: String?,
    val thumbnailUrl: String?,
    val previewAvailable: Boolean
)

data class ChatSync(
    val version: Long,
    val unreadCount: Int,
    val hasInboxChanges: Boolean,
    val serverTimeUtc: String?
)

data class ChatWait(
    val hasChanges: Boolean,
    val currentVersion: Long,
    val serverTimeUtc: String?
)

data class SendMessageResult(
    val messageId: String?,
    val conversationId: String?,
    val deliveryStatus: DeliveryStatus,
    val createdAtUtc: String?
)

data class MessagesPage(
    val conversationId: String?,
    val hasMore: Boolean,
    val messages: List<ChatMessage>
)

enum class MessageType(val id: Int) {
    TEXT(1),
    IMAGE(2),
    PDF(3),
    WORD(4),
    UNKNOWN(-1);

    companion object {
        fun fromId(id: Int): MessageType =
            entries.firstOrNull { it.id == id } ?: UNKNOWN
    }
}

enum class SenderType(val id: Int) {
    CUSTOMER(1),
    BANKER(2),
    UNKNOWN(-1);

    companion object {
        fun fromId(id: Int): SenderType =
            entries.firstOrNull { it.id == id } ?: UNKNOWN
    }
}

enum class DeliveryStatus(val id: Int) {
    SENT(1),
    DELIVERED(2),
    READ(3),
    FAILED(-1),
    UNKNOWN(0);

    companion object {
        fun fromId(id: Int): DeliveryStatus =
            entries.firstOrNull { it.id == id } ?: UNKNOWN
    }
}

data class PendingRating(
    val hasPendingRating: Boolean,
    val conversationId: String?,
    val rateCategoryId: Long?
)
data class CheckConversationResult(
    val hasConversation: Boolean,
    val hasActiveConversation: Boolean,
    val categoryId: String?,
    val categoryTitle: String?
)


data class FileInfo(
    val fileId: String,
    val fileName: String,
    val mimeType: String,
    val fileSize: Long,
    val previewAvailable: Boolean
)

data class DownloadedFile(
    val uri: Uri,
    val fileName: String,
    val mimeType: String
)

data class ChatProfile(
    val fullName: String = "",
    val avatar: String? = null,
    val branchName: String = "",
    val identifier: String = ""
)
