package com.smartbank.smarthamrah.features.profile.component

import com.smartbank.smarthamrah.features.profile.model.ProfileInfoItem


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.smartbank.smarthamrah.R
import com.smartbank.smarthamrah.features.profile.ProfileUiState

@Composable
fun ProfileInfoCard(
    uiState: ProfileUiState
) {

    val items = listOf(
        ProfileInfoItem(
            titleRes = R.string.profile_mobile,
            value = uiState.mobile,
            iconRes =R.drawable.mobile
        ),
        ProfileInfoItem(
            titleRes = R.string.profile_position,
            value = uiState.position,
            iconRes =R.drawable.orgenization
        ),
        ProfileInfoItem(
            titleRes = R.string.profile_province,
            value = uiState.province,
            iconRes =R.drawable.country
        ),
        ProfileInfoItem(
            titleRes = R.string.profile_city,
            value = uiState.city,
            iconRes =R.drawable.province
        ),
        ProfileInfoItem(
            titleRes = R.string.profile_branch,
            value = uiState.branch,
            iconRes =R.drawable.branch
        )
    )

    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            items.forEach { item ->
                InfoRow(
                    title = stringResource(item.titleRes),
                    value = item.value,
                    iconRes = item.iconRes
                )
            }
        }
    }
}