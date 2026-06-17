package com.smartbank.smarthamrah.core.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class NavigationItem(
    val route: String,
    @StringRes val labelRes: Int,
    @DrawableRes val iconRes: Int
)