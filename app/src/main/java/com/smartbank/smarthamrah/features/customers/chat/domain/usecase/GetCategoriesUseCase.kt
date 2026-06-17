package com.smartbank.smarthamrah.features.customers.chat.domain.usecase

import android.net.Uri
import com.smartbank.smarthamrah.features.customers.chat.domain.model.MessageType
import com.smartbank.smarthamrah.features.customers.chat.domain.repository.ChatRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject
import com.smartbank.smarthamrah.features.customers.chat.domain.model.CheckConversationResult

class CheckConversationUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(customerId: Long? = null): CheckConversationResult {
        return repository.checkConversation(customerId)
    }
}
class GetCategoriesUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke() = repository.getCategories()
}

class GetMessagesUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(
        customerId: Long?,
        categoryId:Long?,
        beforeMessageId: String? = null,
        take: Int = 30
    ) = repository.getMessages(customerId, categoryId,beforeMessageId)
}

class SendTextMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(
        customerId: Long?,
        categoryId: Long,
        text: String,
        clientMessageId: String = UUID.randomUUID().toString()
    ) = repository.sendTextMessage(
        customerId = customerId,
        categoryId = categoryId,
        clientMessageId = clientMessageId,
        text = text
    )
}

class SendFileMessageUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(
        customerId: Long?,
        categoryId: Long,
        uri: Uri,
        messageType: MessageType,
        clientMessageId: String = UUID.randomUUID().toString()
    ) = run {
        val uploadedFile = repository.uploadFile(uri)
        val fileId = requireNotNull(uploadedFile.fileId) { "شناسه فایل از سرور دریافت نشد." }

        repository.sendFileMessage(
            customerId = customerId,
            categoryId = categoryId,
            clientMessageId = clientMessageId,
            messageType = messageType,
            fileId = fileId
        )
    }
}

class MarkChatAsReadUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(conversationId: String?) {
        if (!conversationId.isNullOrBlank()) {
            repository.markAsRead(conversationId)
        }
    }
}

class SyncChatUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke() = repository.sync()
}

class ObserveChatChangesUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    operator fun invoke(initialVersion: Long): Flow<Long> = flow {
        var version = initialVersion

        while (true) {
            try {
                val wait = repository.waitForChanges(version)

                if (wait.hasChanges) {
                    val sync = repository.sync()
                    version = sync.version
                    emit(version)
                } else {
                    version = wait.currentVersion
                }
            } catch (e: Exception) {
                delay(3000)
            }
        }
    }
}


class GetChatProfileUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(customerId: Long? = null) = repository.getChatProfile(customerId)
}
class CheckPendingRatingUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke() =
        repository.checkPendingRating()
}

class RateConversationUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(
        conversationId: String,
        rate: Int,
        comment: String? = null
    ) {
        repository.rateConversation(
            conversationId,
            rate,
            comment
        )
    }
}