package com.smartbank.smarthamrah.core.navigation


import androidx.lifecycle.ViewModel
import com.smartbank.smarthamrah.core.auth.AuthSessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavigatorViewModel @Inject constructor(
    val authSessionManager: AuthSessionManager
) : ViewModel()