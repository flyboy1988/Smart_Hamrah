package com.smartbank.smarthamrah.features.customers.chat.domain.repository

import android.net.Uri
import com.smartbank.smarthamrah.features.customers.chat.domain.model.*
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

interface ChatRepository {
    fun observeBankerInbox(): Flow<List<BankerInboxItem>>

    suspend fun refreshBankerInbox()

    suspend fun clearBankerInbox()

    suspend fun clearUnread(customerId: Long)
    suspend fun getCategories(): List<ChatCategory>

    suspend fun sendTextMessage(
        customerId: Long?,
        categoryId: Long,
        clientMessageId: String,
        text: String
    ): SendMessageResult

    suspend fun sendFileMessage(
        customerId: Long?,
        categoryId: Long,
        clientMessageId: String,
        messageType: MessageType,
        fileId: String
    ): SendMessageResult

    suspend fun getMessages(
        customerId: Long? = null,
        categoryId: Long? = null,
        beforeMessageId: String? = null
    ): MessagesPage

    suspend fun markAsRead(conversationId: String)

    suspend fun sync(): ChatSync

    suspend fun waitForChanges(version: Long): ChatWait

    suspend fun uploadFile(uri: Uri): ChatFile

    suspend fun checkPendingRating(): PendingRating

    suspend fun checkConversation(customerId: Long? = null): CheckConversationResult
    suspend fun downloadFile(fileId: String): ResponseBody
    suspend fun getFileInfo(fileId: String): FileInfo
    suspend fun getChatProfile(customerId: Long? = null): ChatProfile

    suspend fun rateConversation(
        conversationId: String,
        rate: Int,
        comment: String? = null
    )
}
