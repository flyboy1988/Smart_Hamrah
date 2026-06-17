package com.smartbank.smarthamrah.core.chat

import com.smartbank.smarthamrah.core.auth.AuthSessionManager
import com.smartbank.smarthamrah.core.preferences.ChatPreferences
import com.smartbank.smarthamrah.features.customers.chat.domain.repository.ChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatSyncManager @Inject constructor(
    private val repository: ChatRepository,
    private val preferences: ChatPreferences,
    private val authSessionManager: AuthSessionManager
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var waitJob: Job? = null
    private var sessionExpired = false

    private val _events = MutableSharedFlow<ChatEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    @Synchronized
    fun start() {
        if (waitJob?.isActive == true) return
        if (sessionExpired) return

        waitJob = scope.launch {
            val token = preferences.getAccessToken()

            if (token.isNullOrBlank()) {
                stopAndExpireSession()
                return@launch
            }

            runCatching {
                initializeSync()
                startWaitLoop()
            }.onFailure { throwable ->
                if (throwable.isUnauthorized()) {
                    stopAndExpireSession()
                } else {
                    _events.tryEmit(
                        ChatEvent.Error(
                            throwable.message ?: "خطا در شروع همگام‌سازی چت"
                        )
                    )
                    waitJob = null
                }
            }
        }
    }

    @Synchronized
    fun stop() {
        waitJob?.cancel()
        waitJob = null
    }

    @Synchronized
    fun resetSession() {
        sessionExpired = false
    }

    private suspend fun initializeSync() {
        val sync = repository.sync()
        preferences.saveChatVersion(sync.version)

        _events.emit(
            ChatEvent.SyncCompleted(
                version = sync.version,
                unreadCount = sync.unreadCount,
                hasInboxChanges = sync.hasInboxChanges
            )
        )
    }

    private suspend fun startWaitLoop() {
        while (currentCoroutineContext().isActive && !sessionExpired) {
            try {
                val token = preferences.getAccessToken()

                if (token.isNullOrBlank()) {
                    stopAndExpireSession()
                    break
                }

                val version = preferences.getChatVersion()
                val wait = repository.waitForChanges(version)

                preferences.saveChatVersion(wait.currentVersion)

                if (wait.hasChanges) {
                    handleChanges()
                }

            } catch (e: Exception) {
                when {
                    e.isUnauthorized() -> {
                        stopAndExpireSession()
                        break
                    }

                    e is SocketTimeoutException -> {
                        handleTemporaryTimeout()
                    }

                    e.isAnotherWaitActive() -> {
                        delay(10_000)
                    }

                    else -> {
                        delay(3_000)
                    }
                }
            }
        }
    }

    private suspend fun handleChanges() {
        val sync = repository.sync()
        preferences.saveChatVersion(sync.version)

        _events.emit(
            ChatEvent.DataChanged(
                version = sync.version,
                unreadCount = sync.unreadCount,
                hasInboxChanges = sync.hasInboxChanges
            )
        )
    }

    /**
     * موقت برای تست:
     * وقتی wait timeout می‌شود، sync می‌زنیم و UI را مجبور به refreshMessages می‌کنیم.
     * نسخه نهایی بهتر است فقط بر اساس hasChanges=true کار کند.
     */
    private suspend fun handleTemporaryTimeout() {
        runCatching {
//            val sync = repository.sync()
//            preferences.saveChatVersion(sync.version)
//
//            _events.emit(
//                ChatEvent.DataChanged(
//                    version = sync.version,
//                    unreadCount = sync.unreadCount,
//                    hasInboxChanges = sync.hasInboxChanges
//                )
//            )
        }

//        delay(3_000)
    }

    @Synchronized
    private fun stopAndExpireSession() {
        if (sessionExpired) return

        sessionExpired = true
        waitJob?.cancel()
        waitJob = null

        scope.launch {
            preferences.clearTokens()
            authSessionManager.notifySessionExpired()
        }
    }

    private fun Throwable.isUnauthorized(): Boolean {
        return this is HttpException && code() == 401
    }
}

private fun Throwable.isAnotherWaitActive(): Boolean {
    if (this !is HttpException) return false
    if (code() != 400) return false

    val errorBody = response()?.errorBody()?.string().orEmpty()
    return errorBody.contains("درخواست انتظار فعال دیگری")
}