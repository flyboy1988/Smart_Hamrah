package com.smartbank.smarthamrah.features.splash


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartbank.smarthamrah.core.chat.ChatSyncManager
import com.smartbank.smarthamrah.core.preferences.ChatPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferences: ChatPreferences,
    private val chatSyncManager: ChatSyncManager
) : ViewModel() {

    private val _navigation = MutableSharedFlow<SplashNavigation>()
    val navigation = _navigation.asSharedFlow()

    fun checkLogin() {
        viewModelScope.launch {
            val token = preferences.getAccessToken()

            if (!token.isNullOrBlank()) {
                chatSyncManager.start()
                _navigation.emit(SplashNavigation.Home)
            } else {
           _navigation.emit(SplashNavigation.Login)

            }
        }
    }
}

sealed interface SplashNavigation {
    data object Home : SplashNavigation
    data object Login : SplashNavigation
}