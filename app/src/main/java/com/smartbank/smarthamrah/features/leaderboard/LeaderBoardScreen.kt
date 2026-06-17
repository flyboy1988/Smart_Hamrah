package com.smartbank.smarthamrah.features.leaderboard


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.smartbank.smarthamrah.R
import com.smartbank.smarthamrah.core.navigation.Screens

@Composable
fun LeaderBoardScreen(modifier: Modifier = Modifier) { // Add this parameter
    Box(
        modifier = modifier.fillMaxSize(), // Use the passed modifier here
        contentAlignment = Alignment.Center
    ) {
        Text(stringResource(R.string.nav_leaderboard))
    }
}