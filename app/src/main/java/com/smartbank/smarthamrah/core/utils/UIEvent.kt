package com.smartbank.smarthamrah.core.utils

sealed class UiEvent {
    data class ShowToast(val message: String, val type: ToastType) : UiEvent()

    data class ShowSnackbar(
        val message: String,
        val type: SnackbarType,
        val actionLabel: String? = null
    ) : UiEvent()

    data class Navigate(val route: String) : UiEvent()
}