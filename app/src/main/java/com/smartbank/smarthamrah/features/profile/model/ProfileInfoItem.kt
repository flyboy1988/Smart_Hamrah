package com.smartbank.smarthamrah.features.profile.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

data class ProfileInfoItem(
    @StringRes val titleRes: Int,
    val value: String,
    @DrawableRes val iconRes: Int
)