package com.smartbank.smarthamrah.core.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.smartBankDataStore by preferencesDataStore(
    name = "smartbank_preferences"
)

@Singleton
class ChatPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private companion object {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val ACCESS_TOKEN_EXPIRES_AT = stringPreferencesKey("access_token_expires_at")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val REFRESH_TOKEN_EXPIRES_AT = stringPreferencesKey("refresh_token_expires_at")
        val CHAT_VERSION = longPreferencesKey("chat_version")
    }

    suspend fun saveAccessToken(token: String) {
        context.smartBankDataStore.edit {
            it[ACCESS_TOKEN] = token
        }
    }

    suspend fun getAccessToken(): String? {
        return context.smartBankDataStore.data
            .map { it[ACCESS_TOKEN] }
            .first()
    }

    suspend fun saveAccessTokenExpiresAt(value: String?) {
        context.smartBankDataStore.edit {
            if (value == null) it.remove(ACCESS_TOKEN_EXPIRES_AT)
            else it[ACCESS_TOKEN_EXPIRES_AT] = value
        }
    }

    suspend fun getAccessTokenExpiresAt(): String? {
        return context.smartBankDataStore.data
            .map { it[ACCESS_TOKEN_EXPIRES_AT] }
            .first()
    }

    suspend fun saveRefreshToken(token: String) {
        context.smartBankDataStore.edit {
            it[REFRESH_TOKEN] = token
        }
    }

    suspend fun getRefreshToken(): String? {
        return context.smartBankDataStore.data
            .map { it[REFRESH_TOKEN] }
            .first()
    }

    suspend fun saveRefreshTokenExpiresAt(value: String?) {
        context.smartBankDataStore.edit {
            if (value == null) it.remove(REFRESH_TOKEN_EXPIRES_AT)
            else it[REFRESH_TOKEN_EXPIRES_AT] = value
        }
    }

    suspend fun getRefreshTokenExpiresAt(): String? {
        return context.smartBankDataStore.data
            .map { it[REFRESH_TOKEN_EXPIRES_AT] }
            .first()
    }

    suspend fun saveChatVersion(version: Long) {
        context.smartBankDataStore.edit {
            it[CHAT_VERSION] = version
        }
    }

    suspend fun getChatVersion(): Long {
        return context.smartBankDataStore.data
            .map { it[CHAT_VERSION] ?: 0L }
            .first()
    }

    suspend fun clearTokens() {
        context.smartBankDataStore.edit {
            it.remove(ACCESS_TOKEN)
            it.remove(ACCESS_TOKEN_EXPIRES_AT)
            it.remove(REFRESH_TOKEN)
            it.remove(REFRESH_TOKEN_EXPIRES_AT)
        }
    }

    suspend fun clearAll() {
        context.smartBankDataStore.edit {
            it.clear()
        }
    }
}
