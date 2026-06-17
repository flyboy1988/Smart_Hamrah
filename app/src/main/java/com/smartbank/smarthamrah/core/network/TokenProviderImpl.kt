package com.smartbank.smarthamrah.core.network

import com.smartbank.smarthamrah.core.preferences.ChatPreferences
import javax.inject.Inject

class TokenProviderImpl @Inject constructor(
    private val preferences: ChatPreferences
) : TokenProvider {

    override suspend fun getAccessToken(): String? {
        return preferences.getAccessToken()
    }

    override suspend fun getRefreshToken(): String? {
        return preferences.getRefreshToken()
    }

    override suspend fun saveTokens(
        accessToken: String,
        refreshToken: String?
    ) {
        preferences.saveAccessToken(accessToken)

        refreshToken?.let {
            preferences.saveRefreshToken(it)
        }
    }

    override suspend fun clearTokens() {
        preferences.clearTokens()
    }
}
