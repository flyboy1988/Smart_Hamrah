package com.smartbank.smarthamrah.core.chat

sealed interface ChatEvent {

    data class SyncCompleted(
        val version: Long,
        val unreadCount: Int,
        val hasInboxChanges: Boolean
    ) : ChatEvent

    data class DataChanged(
        val version: Long,
        val unreadCount: Int,
        val hasInboxChanges: Boolean
    ) : ChatEvent

    data class Error(
        val message: String
    ) : ChatEvent
}