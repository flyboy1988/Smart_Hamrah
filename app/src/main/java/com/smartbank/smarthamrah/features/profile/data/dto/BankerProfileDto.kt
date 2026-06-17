package com.smartbank.smarthamrah.features.profile.data.dto

import com.google.gson.annotations.SerializedName

data class BankerProfileDto(
    @SerializedName("nationalCode")
    val nationalCode: String?,
    @SerializedName("mobileNumber")
    val mobileNumber: String?,
    @SerializedName("branchName")
    val branchName: String?,
    @SerializedName("branchCode")
    val branchCode: String?,
    @SerializedName("organizationPosition")
    val organizationPosition: String?,
    @SerializedName("avatarFileId")
    val avatarFileId: String?,
    @SerializedName("personalNumber")
    val personalNumber: String?,
    @SerializedName("activityCityTitle")
    val activityCityTitle: String?,
    @SerializedName("activityProvinceTitle")
    val activityProvinceTitle: String?,
    @SerializedName("fullName")
    val fullName: String?
)
