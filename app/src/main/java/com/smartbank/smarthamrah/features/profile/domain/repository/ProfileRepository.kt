package com.smartbank.smarthamrah.features.profile.domain.repository

import com.smartbank.smarthamrah.features.profile.domain.model.BankerProfile

interface ProfileRepository {
    suspend fun getBankerProfile(): BankerProfile
}
