package com.smartbank.smarthamrah.features.customers.chat.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "banker_inbox")
data class BankerInboxEntity(
    @PrimaryKey
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