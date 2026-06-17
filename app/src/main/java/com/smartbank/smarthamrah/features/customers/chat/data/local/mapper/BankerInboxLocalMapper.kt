package com.smartbank.smarthamrah.features.customers.chat.data.local.mapper

import com.smartbank.smarthamrah.features.customers.chat.data.dto.BankerChatItemDto
import com.smartbank.smarthamrah.features.customers.chat.data.local.entity.BankerInboxEntity
import com.smartbank.smarthamrah.features.customers.chat.domain.model.BankerInboxItem

fun BankerChatItemDto.toEntity(): BankerInboxEntity {
    return BankerInboxEntity(
        customerId = customerId,
        customerName = customerName,
        conversationId = conversationId,
        conversationStatusId = conversationStatusId,
        conversationStatusTitle = conversationStatusTitle,
        lastMessage = lastMessage,
        lastMessageAtUtc = lastMessageAtUtc,
        unreadCount = unreadCount,
        hasActiveConversation = hasActiveConversation
    )
}

fun BankerInboxEntity.toDomain(): BankerInboxItem {
    return BankerInboxItem(
        customerId = customerId,
        customerName = customerName,
        conversationId = conversationId,
        conversationStatusId = conversationStatusId,
        conversationStatusTitle = conversationStatusTitle,
        lastMessage = lastMessage,
        lastMessageAtUtc = lastMessageAtUtc,
        unreadCount = unreadCount,
        hasActiveConversation = hasActiveConversation
    )
}