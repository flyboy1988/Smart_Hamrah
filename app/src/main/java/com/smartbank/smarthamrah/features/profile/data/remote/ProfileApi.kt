package com.smartbank.smarthamrah.features.profile.data.remote

import com.smartbank.smarthamrah.features.profile.data.dto.BankerProfileDto
import retrofit2.http.GET

interface ProfileApi {

    @GET("api/v1/Users/banker-profile")
    suspend fun getBankerProfile(): BankerProfileDto
}
