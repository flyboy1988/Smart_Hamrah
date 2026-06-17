package com.smartbank.smarthamrah.features.profile.data.repository

import com.smartbank.smarthamrah.features.profile.data.mapper.toDomain
import com.smartbank.smarthamrah.features.profile.data.remote.ProfileApi
import com.smartbank.smarthamrah.features.profile.domain.model.BankerProfile
import com.smartbank.smarthamrah.features.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: ProfileApi
) : ProfileRepository {

    override suspend fun getBankerProfile(): BankerProfile {
        return api.getBankerProfile().toDomain()
    }
}
