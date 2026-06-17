package com.smartbank.smarthamrah.features.customers.chat.domain.model


data class BankerInboxItem(
    val customerId: Long,
    val customerName: String?,
    val conversationId: String?,
    val conversationStatusId: Int?,
    val conversationStatusTitle: String?,
    val lastMessage: String?,
    val lastMessageAtUtc: String?,
    val unreadCount: Int,
    val hasActiveConversation: Boolean
)