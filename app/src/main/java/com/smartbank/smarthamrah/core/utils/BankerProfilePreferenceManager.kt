package com.smartbank.smarthamrah.core.utils

import android.content.Context
import com.smartbank.smarthamrah.features.profile.domain.model.BankerProfile

object BankerProfilePreferenceManager {

    private const val PREF_NAME = "banker_profile_pref"

    private const val KEY_NATIONAL_CODE = "nationalCode"
    private const val KEY_MOBILE_NUMBER = "mobileNumber"
    private const val KEY_BRANCH_NAME = "branchName"
    private const val KEY_BRANCH_CODE = "branchCode"
    private const val KEY_ORGANIZATION_POSITION = "organizationPosition"
    private const val KEY_AVATAR_FILE_ID = "avatarFileId"
    private const val KEY_PERSONAL_NUMBER = "personalNumber"
    private const val KEY_ACTIVITY_CITY_TITLE = "activityCityTitle"
    private const val KEY_ACTIVITY_PROVINCE_TITLE = "activityProvinceTitle"
    private const val KEY_FULL_NAME = "fullName"

    fun saveProfile(context: Context, profile: BankerProfile) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_NATIONAL_CODE, profile.nationalCode)
            .putString(KEY_MOBILE_NUMBER, profile.mobileNumber)
            .putString(KEY_BRANCH_NAME, profile.branchName)
            .putString(KEY_BRANCH_CODE, profile.branchCode)
            .putString(KEY_ORGANIZATION_POSITION, profile.organizationPosition)
            .putString(KEY_AVATAR_FILE_ID, profile.avatarFileId)
            .putString(KEY_PERSONAL_NUMBER, profile.personalNumber)
            .putString(KEY_ACTIVITY_CITY_TITLE, profile.activityCityTitle)
            .putString(KEY_ACTIVITY_PROVINCE_TITLE, profile.activityProvinceTitle)
            .putString(KEY_FULL_NAME, profile.fullName)
            .apply()
    }

    fun getProfile(context: Context): BankerProfile {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        return BankerProfile(
            nationalCode = pref.getString(KEY_NATIONAL_CODE, "").orEmpty(),
            mobileNumber = pref.getString(KEY_MOBILE_NUMBER, "").orEmpty(),
            branchName = pref.getString(KEY_BRANCH_NAME, "").orEmpty(),
            branchCode = pref.getString(KEY_BRANCH_CODE, "").orEmpty(),
            organizationPosition = pref.getString(KEY_ORGANIZATION_POSITION, "").orEmpty(),
            avatarFileId = pref.getString(KEY_AVATAR_FILE_ID, null),
            personalNumber = pref.getString(KEY_PERSONAL_NUMBER, "").orEmpty(),
            activityCityTitle = pref.getString(KEY_ACTIVITY_CITY_TITLE, "").orEmpty(),
            activityProvinceTitle = pref.getString(KEY_ACTIVITY_PROVINCE_TITLE, "").orEmpty(),
            fullName = pref.getString(KEY_FULL_NAME, "").orEmpty()
        )
    }

    fun clearProfile(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }
}
