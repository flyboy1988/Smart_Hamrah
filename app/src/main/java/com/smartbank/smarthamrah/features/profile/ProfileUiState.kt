package com.smartbank.smarthamrah.features.profile

data class ProfileUiState(
    val fullName: String = "",
    val personnelCode: String = "",
    val mobile: String = "",
    val position: String = "",
    val province: String = "",
    val city: String = "",
    val branch: String = "",
    val score: Float = 0f,
    val voteCount: Int = 0
)