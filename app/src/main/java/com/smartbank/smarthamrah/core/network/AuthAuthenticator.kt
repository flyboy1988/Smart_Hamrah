package com.smartbank.smarthamrah.core.network

import com.smartbank.smarthamrah.core.auth.AuthSessionManager
import com.smartbank.smarthamrah.features.auth.data.dto.RefreshTokenRequestDto
import com.smartbank.smarthamrah.features.auth.data.remote.AuthApi
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val tokenProvider: TokenProvider,
    private val authApi: dagger.Lazy<AuthApi>,
    private val authSessionManager: AuthSessionManager
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) {
            clearSession()
            return null
        }

        val newAccessToken = runBlocking {
            val refreshToken = tokenProvider.getRefreshToken()

            if (refreshToken.isNullOrBlank()) {
                clearSession()
                return@runBlocking null
            }

            runCatching {
                val refreshResponse = authApi.get().refreshToken(
                    RefreshTokenRequestDto(refreshToken = refreshToken)
                )

                val accessToken = refreshResponse.accessToken
                val newRefreshToken = refreshResponse.refreshToken

                if (accessToken.isNullOrBlank()) {
                    clearSession()
                    return@runCatching null
                }

                tokenProvider.saveTokens(
                    accessToken = accessToken,
                    refreshToken = newRefreshToken ?: refreshToken
                )

                accessToken
            }.getOrElse {
                clearSession()
                null
            }
        } ?: return null

        return response.request
            .newBuilder()
            .header("Authorization", "Bearer $newAccessToken")
            .build()
    }

    private fun clearSession() {
        runBlocking {
            tokenProvider.clearTokens()
        }
        authSessionManager.notifySessionExpired()
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var priorResponse = response.priorResponse

        while (priorResponse != null) {
            count++
            priorResponse = priorResponse.priorResponse
        }

        return count
    }
}