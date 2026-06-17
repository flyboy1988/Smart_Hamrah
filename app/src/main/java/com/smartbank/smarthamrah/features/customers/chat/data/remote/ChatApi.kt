package com.smartbank.smarthamrah.features.customers.chat.data.remote

import com.smartbank.smarthamrah.features.customers.chat.data.dto.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatApi {

    @GET("api/v1/Chat/categories")
    suspend fun getCategories(): GetCategoriesResponseDto

    @POST("api/v1/Chat/send")
    suspend fun sendMessage(
        @Body request: SendMessageRequestDto
    ): SendMessageResponseDto

    @POST("api/v1/Chat/messages")
    suspend fun getMessages(
        @Body request: GetMessagesRequestDto
    ): GetMessagesResponseDto

    @POST("api/v1/Chat/read")
    suspend fun markAsRead(
        @Body request: MarkAsReadRequestDto
    ): Response<Unit>

    @GET("api/v1/Chat/unread-count")
    suspend fun getUnreadCount(): GetUnreadCountResponseDto

    @GET("api/v1/Chat/banker-inbox")
    suspend fun getBankerInbox(): GetBankerChatListResponseDto

    @POST("api/v1/Chat/close-conversation")
    suspend fun closeConversation(
        @Body request: CloseConversationRequestDto
    ): Response<Unit>

    @GET("api/v1/Chat/wait")
    suspend fun waitForChanges(
        @Query("version") version: Long
    ): ChatWaitResponseDto

    @GET("api/v1/Chat/sync")
    suspend fun sync(): ChatSyncResponseDto

    @POST("api/v1/Chat/chat-profile")
    suspend fun getChatProfile(
        @Body request: GetChatProfileRequestDto = GetChatProfileRequestDto()
    ): ChatProfileResponseDto

    @POST("api/v1/Chat/check-conversation")
    suspend fun checkConversation(
        @Body request: CheckConversationRequestDto
    ): CheckConversationResponseDto

    @GET("api/v1/Chat/pending-rating")
    suspend fun pendingRating(): PendingRatingResponseDto

    @POST("api/v1/Chat/rate-conversation")
    suspend fun rateConversation(
        @Body request: RateConversationRequestDto
    ): RateConversationResponseDto
}
