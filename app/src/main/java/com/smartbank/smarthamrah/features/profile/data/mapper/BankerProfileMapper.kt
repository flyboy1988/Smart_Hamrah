package com.smartbank.smarthamrah.features.profile.data.mapper

import com.smartbank.smarthamrah.features.profile.data.dto.BankerProfileDto
import com.smartbank.smarthamrah.features.profile.domain.model.BankerProfile

fun BankerProfileDto.toDomain(): BankerProfile {
    return BankerProfile(
        nationalCode = nationalCode.orEmpty(),
        mobileNumber = mobileNumber.orEmpty(),
        branchName = branchName.orEmpty(),
        branchCode = branchCode.orEmpty(),
        organizationPosition = organizationPosition.orEmpty(),
        avatarFileId = avatarFileId,
        personalNumber = personalNumber.orEmpty(),
        activityCityTitle = activityCityTitle.orEmpty(),
        activityProvinceTitle = activityProvinceTitle.orEmpty(),
        fullName = fullName.orEmpty()
    )
}
