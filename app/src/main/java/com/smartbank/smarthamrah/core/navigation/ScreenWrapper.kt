package com.smartbank.smarthamrah.core.navigation


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun ScreenWrapper(
    title: String,
    navController: NavController,
    showBackButton: Boolean = true,
    content: @Composable (Modifier) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Your custom AppToolbar acts as the header
        AppToolbar(
            title = title,
            navController = navController,
            showBackButton = showBackButton
        )

        // The rest of the screen content
        Box(modifier = Modifier.weight(1f)) {
            content(Modifier)
        }
    }
}