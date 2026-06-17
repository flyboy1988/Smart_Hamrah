package com.smartbank.smarthamrah.features.auth.data.remote

import com.smartbank.smarthamrah.features.auth.data.dto.*
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("api/v1/Auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): LoginResponseDto

    @POST("api/v1/Auth/verify-otp")
    suspend fun verifyOtp(
        @Body request: VerifyOtpRequestDto
    ): VerifyOtpResponseDto

    @POST("api/v1/Auth/refresh-token")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequestDto
    ): RefreshTokenResponseDto

    @POST("api/v1/Auth/logout")
    suspend fun logout(
        @Body request: LogoutRequestDto
    ): LogoutResponseDto
}
