package com.smartbank.smarthamrah.features.profile.domain.usecase

import com.smartbank.smarthamrah.features.profile.domain.model.BankerProfile
import com.smartbank.smarthamrah.features.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class GetBankerProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): BankerProfile {
        return repository.getBankerProfile()
    }
}
