package com.smartbank.smarthamrah.features.customers.chat.data.mapper

import com.smartbank.smarthamrah.features.customers.chat.data.dto.*
import com.smartbank.smarthamrah.features.customers.chat.domain.model.*
import com.smartbank.smarthamrah.features.customers.chat.data.dto.CheckConversationResponseDto
import com.smartbank.smarthamrah.features.customers.chat.domain.model.CheckConversationResult

fun CheckConversationResponseDto.toDomain(): CheckConversationResult {
    return CheckConversationResult(
        hasConversation = hasConversation,
        hasActiveConversation = hasActiveConversation,
        categoryId = categoryId,
        categoryTitle = categoryTitle
    )
}
fun CategoryItemDto.toDomain(): ChatCategory =
    ChatCategory(
        id = categoryId,
        title = title.orEmpty()
    )

fun ChatSyncResponseDto.toDomain(): ChatSync =
    ChatSync(
        version = version,
        unreadCount = unreadCount,
        hasInboxChanges = hasInboxChanges,
        serverTimeUtc = serverTimeUtc
    )

fun ChatWaitResponseDto.toDomain(): ChatWait =
    ChatWait(
        hasChanges = hasChanges,
        currentVersion = currentVersion,
        serverTimeUtc = serverTimeUtc
    )

fun SendMessageResponseDto.toDomain(): SendMessageResult =
    SendMessageResult(
        messageId = messageId,
        conversationId = conversationId,
        deliveryStatus = DeliveryStatus.fromId(deliveryStatusId),
        createdAtUtc = createdAtUtc
    )

fun GetMessagesResponseDto.toDomain(): MessagesPage =
    MessagesPage(
        conversationId = conversationId,
        hasMore = hasMore,
        messages = messages.orEmpty().map { it.toDomain() }
    )

fun MessageItemDto.toDomain(): ChatMessage {
    val hasFile = fileId != null || downloadUrl != null || attachmentUrl != null

    return ChatMessage(
        id = messageId.orEmpty(),
        conversationId = conversationId,
        senderId = senderId,
        senderType = when (senderTypeTitle?.uppercase()) {
            "BANKER" -> SenderType.BANKER
            "CUSTOMER" -> SenderType.CUSTOMER
            else -> SenderType.fromId(senderTypeId)
        },        senderName = senderName.orEmpty(),
        messageType = MessageType.fromId(messageTypeId),
        text = text,
        file = if (hasFile) {
            ChatFile(
                fileId = fileId,
                fileName = fileName,
                fileSize = fileSize,
                mimeType = mimeType,
                downloadUrl = downloadUrl ?: attachmentUrl,
                thumbnailUrl = thumbnailUrl,
                previewAvailable = previewAvailable
            )
        } else {
            null
        },
        deliveryStatus = DeliveryStatus.fromId(deliveryStatusId),
        createdAtUtc = createdAtUtc.orEmpty(),
        categoryId = categoryId,
        categoryTitle = categoryTitle.orEmpty(),
        conversationStatusId = conversationStatusId,
        conversationStatusTitle = conversationStatusTitle.orEmpty()
    )
}

fun PendingRatingResponseDto.toDomain(): PendingRating =
    PendingRating(
        hasPendingRating = hasPendingRating,
        conversationId = conversationId,
        rateCategoryId = categoryId
    )
fun FileInfoResponseDto.toDomain(): FileInfo {
    return FileInfo(
        fileId = fileId.orEmpty(),
        fileName = fileName.orEmpty(),
        mimeType = mimeType.orEmpty(),
        fileSize = fileSize ?: 0L,
        previewAvailable = previewAvailable ?: false
    )
}

fun ChatProfileResponseDto.toDomain(): com.smartbank.smarthamrah.features.customers.chat.domain.model.ChatProfile =
    com.smartbank.smarthamrah.features.customers.chat.domain.model.ChatProfile(
        fullName = fullName.orEmpty(),
        avatar = avatar,
        branchName = branchName.orEmpty(),
        identifier = identifier.orEmpty()
    )
