package com.smartbank.smarthamrah.core.auth


import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthSessionManager @Inject constructor() {

    private val _events = MutableSharedFlow<AuthSessionEvent>(
        extraBufferCapacity = 1
    )
    val events = _events.asSharedFlow()

    fun notifySessionExpired() {
        _events.tryEmit(AuthSessionEvent.SessionExpired)
    }
}

sealed interface AuthSessionEvent {
    data object SessionExpired : AuthSessionEvent
}