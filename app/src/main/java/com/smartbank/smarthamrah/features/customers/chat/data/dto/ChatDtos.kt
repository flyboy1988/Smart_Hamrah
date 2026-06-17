package com.smartbank.smarthamrah.features.customers.chat.data.dto

data class SendMessageRequestDto(
    val customerId: Long? = null,
    val categoryId: Long,
    val clientMessageId: String,
    val messageTypeId: Int,
    val text: String? = null,
    val fileId: String? = null
)

data class SendMessageResponseDto(
    val messageId: String? = null,
    val conversationId: String? = null,
    val deliveryStatusId: Int = 0,
    val deliveryStatusTitle: String? = null,
    val createdAtUtc: String? = null
)

data class GetMessagesRequestDto(
    val customerId: Long? = null,
    val categoryId: Long? = null,
    val beforeMessageId: String? = null,
    val take: Int = 30
)

data class GetMessagesResponseDto(
    val conversationId: String? = null,
    val hasMore: Boolean = false,
    val serverTime: String? = null,
    val messages: List<MessageItemDto>? = emptyList()
)

data class MessageItemDto(
    val messageId: String? = null,
    val senderId: Long = 0,
    val senderTypeId: Int = 0,
    val senderTypeTitle: String? = null,
    val senderName: String? = null,
    val messageTypeId: Int = 0,
    val messageTypeTitle: String? = null,
    val text: String? = null,
    val attachmentUrl: String? = null,
    val thumbnailUrl: String? = null,
    val fileName: String? = null,
    val fileSize: Long? = null,
    val mimeType: String? = null,
    val previewAvailable: Boolean = false,
    val deliveryStatusId: Int = 0,
    val deliveryStatusTitle: String? = null,
    val createdAtUtc: String? = null,
    val conversationId: String? = null,
    val categoryId: Long = 0,
    val categoryTitle: String? = null,
    val conversationStatusId: Int = 0,
    val conversationStatusTitle: String? = null,
    val downloadUrl: String? = null,
    val fileId: String? = null
)

data class MarkAsReadRequestDto(
    val conversationId: String?
)

data class GetUnreadCountResponseDto(
    val totalUnreadCount: Int = 0
)

data class ChatWaitResponseDto(
    val hasChanges: Boolean = false,
    val currentVersion: Long = 0,
    val serverTimeUtc: String? = null
)

data class ChatSyncResponseDto(
    val version: Long = 0,
    val unreadCount: Int = 0,
    val hasInboxChanges: Boolean = false,
    val serverTimeUtc: String? = null
)

data class GetCategoriesResponseDto(
    val items: List<CategoryItemDto>? = emptyList()
)

data class CategoryItemDto(
    val categoryId: Long = 0,
    val title: String? = null
)


data class CloseConversationRequestDto(
    val conversationId: String?
)

data class PendingRatingResponseDto(
    val hasPendingRating: Boolean = false,
    val conversationId: String? = null,
    val bankerId: Long? = null,
    val customerId: Long? = null,
    val categoryId: Long? = null,
    val closedAtUtc: String? = null
)

data class RateConversationRequestDto(
    val conversationId: String?,
    val rate: Int,
    val comment: String? = null
)

data class RateConversationResponseDto(
    val ratingId: Long = 0,
    val conversationId: String? = null,
    val rate: Int = 0,
    val createdAtUtc: String? = null
)

data class BankerChatItemDto(
    val customerId: Long = 0,
    val customerName: String? = null,
    val conversationId: String? = null,
    val conversationStatusId: Int? = null,
    val conversationStatusTitle: String? = null,
    val lastMessage: String? = null,
    val lastMessageAtUtc: String? = null,
    val unreadCount: Int = 0,
    val hasActiveConversation: Boolean = false
)

data class GetBankerChatListResponseDto(
    val items: List<BankerChatItemDto>? = emptyList()
)

data class UploadFileResponseDto(
    val fileId: String? = null,
    val fileName: String? = null,
    val mimeType: String? = null,
    val fileSize: Long = 0,
    val bucketName: String? = null
)


data class FileInfoResponseDto(
    val fileId: String?,
    val fileName: String?,
    val mimeType: String?,
    val fileSize: Long?,
    val previewAvailable: Boolean?
)
data class ErrorResponseDto(
    val code: Int = 0,
    val message: String? = null,
    val data: Any? = null
)
data class CheckConversationRequestDto(
    val customerId: Long? = null
)
data class CheckConversationResponseDto(
    val hasConversation: Boolean,
    val hasActiveConversation: Boolean,
    val categoryId: String?,
    val categoryTitle: String?
)

data class GetChatProfileRequestDto(
    val customerId: Long? = null
)

data class ChatProfileResponseDto(
    val fullName: String? = null,
    val avatar: String? = null,
    val branchName: String? = null,
    val identifier: String? = null
)
