package com.smartbank.smarthamrah.features.profile.domain.model

data class BankerProfile(
    val nationalCode: String,
    val mobileNumber: String,
    val branchName: String,
    val branchCode: String,
    val organizationPosition: String,
    val avatarFileId: String?,
    val personalNumber: String,
    val activityCityTitle: String,
    val activityProvinceTitle: String,
    val fullName: String
)
