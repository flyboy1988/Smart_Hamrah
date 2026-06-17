package com.smartbank.smarthamrah.core.utils

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.graphics.Color

enum class SnackbarType {
    SUCCESS, ERROR, WARNING, INFO
}

fun getSnackbarColor(type: SnackbarType): Color {
    return when (type) {
        SnackbarType.SUCCESS -> Color(0xFF4CAF50) // Green
        SnackbarType.ERROR -> Color(0xFFF44336)   // Red
        SnackbarType.WARNING -> Color(0xFFFF9800) // Orange
        SnackbarType.INFO -> Color(0xFF1678E7)    // Brand Blue
    }
}

suspend fun SnackbarHostState.showCustomSnackbar(
    message: String,
    actionLabel: String? = null,
    duration: SnackbarDuration = SnackbarDuration.Short
) {
    this.showSnackbar(
        message = message,
        actionLabel = actionLabel,
        duration = duration,
   //     withDismissAction = false
    )
}