package com.smartbank.smarthamrah.features.customers.chat.data.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.smartbank.smarthamrah.features.customers.chat.data.dto.CheckConversationRequestDto
import com.smartbank.smarthamrah.features.customers.chat.data.dto.GetMessagesRequestDto
import com.smartbank.smarthamrah.features.customers.chat.data.dto.MarkAsReadRequestDto
import com.smartbank.smarthamrah.features.customers.chat.data.dto.RateConversationRequestDto
import com.smartbank.smarthamrah.features.customers.chat.data.dto.SendMessageRequestDto
import com.smartbank.smarthamrah.features.customers.chat.data.local.dao.BankerInboxDao
import com.smartbank.smarthamrah.features.customers.chat.data.local.mapper.toDomain
import com.smartbank.smarthamrah.features.customers.chat.data.local.mapper.toEntity
import com.smartbank.smarthamrah.features.customers.chat.data.mapper.toDomain
import com.smartbank.smarthamrah.features.customers.chat.data.remote.ChatApi
import com.smartbank.smarthamrah.features.customers.chat.data.remote.FileApi
import com.smartbank.smarthamrah.features.customers.chat.domain.model.ChatCategory
import com.smartbank.smarthamrah.features.customers.chat.domain.model.ChatFile
import com.smartbank.smarthamrah.features.customers.chat.domain.model.ChatSync
import com.smartbank.smarthamrah.features.customers.chat.domain.model.ChatWait
import com.smartbank.smarthamrah.features.customers.chat.domain.model.MessageType
import com.smartbank.smarthamrah.features.customers.chat.domain.model.MessagesPage
import com.smartbank.smarthamrah.features.customers.chat.domain.model.PendingRating
import com.smartbank.smarthamrah.features.customers.chat.domain.model.SendMessageResult
import com.smartbank.smarthamrah.features.customers.chat.domain.model.BankerInboxItem
import com.smartbank.smarthamrah.features.customers.chat.domain.model.CheckConversationResult
import com.smartbank.smarthamrah.features.customers.chat.domain.model.FileInfo
import com.smartbank.smarthamrah.features.customers.chat.domain.repository.ChatRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import java.util.UUID
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatApi: ChatApi,
    private val fileApi: FileApi,
    private val bankerInboxDao: BankerInboxDao,
    @ApplicationContext private val context: Context
) : ChatRepository {

    override fun observeBankerInbox(): Flow<List<BankerInboxItem>> {
        return bankerInboxDao
            .observeInbox()
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }


    override suspend fun getCategories(): List<ChatCategory> {
        return chatApi.getCategories()
            .items
            .orEmpty()
            .map { it.toDomain() }
    }

    override suspend fun sendTextMessage(
        customerId: Long?,
        categoryId: Long,
        clientMessageId: String,
        text: String
    ): SendMessageResult {
        val request = SendMessageRequestDto(
            customerId = customerId,
            categoryId = categoryId,
            clientMessageId = clientMessageId,
            messageTypeId = MessageType.TEXT.id,
            text = text,
            fileId = null
        )

        return chatApi.sendMessage(request).toDomain()
    }

    override suspend fun sendFileMessage(
        customerId: Long?,
        categoryId: Long,
        clientMessageId: String,
        messageType: MessageType,
        fileId: String
    ): SendMessageResult {
        val request = SendMessageRequestDto(
            customerId = customerId,
            categoryId = categoryId,
            clientMessageId = clientMessageId,
            messageTypeId = messageType.id,
            text = null,
            fileId = fileId
        )

        return chatApi.sendMessage(request).toDomain()
    }

    override suspend fun getMessages(
        customerId: Long?,
        categoryId: Long?,
        beforeMessageId: String?
    ): MessagesPage {
        return chatApi.getMessages(
            GetMessagesRequestDto(
                customerId = customerId,
                categoryId = categoryId,
                beforeMessageId = beforeMessageId,
                take = 30
            )
        ).toDomain()
    }


    override suspend fun markAsRead(conversationId: String) {
        chatApi.markAsRead(
            MarkAsReadRequestDto(conversationId)
        )
    }

    override suspend fun sync(): ChatSync {
        return chatApi.sync().toDomain()
    }

    override suspend fun waitForChanges(version: Long): ChatWait {
        return chatApi.waitForChanges(version).toDomain()
    }

    override suspend fun uploadFile(uri: Uri): ChatFile {
        val mimeType = getMimeType(uri)
        val fileName = getFileName(uri)
        val fileBytes = readFileBytes(uri)

        val requestBody = fileBytes.toRequestBody(
            mimeType.toMediaTypeOrNull()
        )

        val part = MultipartBody.Part.createFormData(
            name = "file",
            filename = fileName,
            body = requestBody
        )

        val response = fileApi.uploadFile(part)

        return ChatFile(
            fileId = response.fileId,
            fileName = response.fileName,
            fileSize = response.fileSize,
            mimeType = response.mimeType,
            downloadUrl = response.fileId?.let { fileId ->
                "api/v1/Files/$fileId"
            },
            thumbnailUrl = null,
            previewAvailable = response.mimeType == "image/jpeg" ||
                    response.mimeType == "image/png"
        )
    }

    override suspend fun checkPendingRating(): PendingRating {
        return chatApi.pendingRating().toDomain()
    }



    override suspend fun refreshBankerInbox() {
        val response = chatApi.getBankerInbox()

        val entities = response.items
            .orEmpty()
            .map { it.toEntity() }

        bankerInboxDao.upsertAll(entities)
    }

    override suspend fun clearBankerInbox() {
        bankerInboxDao.clearAll()
    }

    override suspend fun clearUnread(customerId: Long) {
        bankerInboxDao.clearUnread(customerId)
    }

    private fun getMimeType(uri: Uri): String {
        return context.contentResolver.getType(uri)
            ?: "application/octet-stream"
    }

    private fun readFileBytes(uri: Uri): ByteArray {
        return context.contentResolver
            .openInputStream(uri)
            ?.use { inputStream ->
                inputStream.readBytes()
            }
            ?: error("فایل انتخاب‌شده قابل خواندن نیست.")
    }

    private fun getFileName(uri: Uri): String {
        return queryFileName(uri)
            ?: "chat-file-${UUID.randomUUID()}"
    }

    private fun queryFileName(uri: Uri): String? {
        return context.contentResolver
            .query(uri, null, null, null, null)
            ?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)

                if (cursor.moveToFirst() && nameIndex >= 0) {
                    cursor.getString(nameIndex)
                } else {
                    null
                }
            }
    }
    override suspend fun checkConversation(
        customerId: Long?
    ): CheckConversationResult {
        return chatApi.checkConversation(
            CheckConversationRequestDto()
        ).toDomain()
    }

    override suspend fun getChatProfile(customerId: Long?): com.smartbank.smarthamrah.features.customers.chat.domain.model.ChatProfile {
        return chatApi.getChatProfile(
            com.smartbank.smarthamrah.features.customers.chat.data.dto.GetChatProfileRequestDto(customerId = customerId)
        ).toDomain()
    }

    override suspend fun rateConversation(
        conversationId: String,
        rate: Int,
        comment: String?
    ) {
        chatApi.rateConversation(
            RateConversationRequestDto(
                conversationId = conversationId,
                rate = rate,
                comment = comment
            )
        )
    }

    override suspend fun downloadFile(fileId: String): ResponseBody {
        return fileApi.downloadFile(fileId)
    }

    override suspend fun getFileInfo(fileId: String): FileInfo {
        return fileApi.getFileInfo(fileId).toDomain()
    }
}