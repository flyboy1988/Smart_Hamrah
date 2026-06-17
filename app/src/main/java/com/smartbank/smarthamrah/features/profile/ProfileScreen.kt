package com.smartbank.smarthamrah.features.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smartbank.smarthamrah.features.profile.component.LogoutCard
import com.smartbank.smarthamrah.features.profile.component.ProfileInfoCard
import com.smartbank.smarthamrah.features.profile.component.ScoreCard
import com.smartbank.smarthamrah.features.profile.component.UserCard

@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    isLogoutLoading: Boolean = false,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        UserCard(
            fullName = uiState.fullName,
            personnelCode = uiState.personnelCode
        )

        Spacer(Modifier.height(16.dp))

        ScoreCard(
            score = uiState.score,
            voteCount = uiState.voteCount
        )

        Spacer(Modifier.height(16.dp))

        ProfileInfoCard(uiState)

        Spacer(Modifier.height(16.dp))

        LogoutCard(
            isLoading = isLogoutLoading,
            onLogoutClick = onLogoutClick
        )
    }
}