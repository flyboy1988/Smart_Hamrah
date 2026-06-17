package com.smartbank.smarthamrah.core.network.model


data class ErrorResponse(
    val code: Int = 0,
    val message: String = "",
    val data: String? = null
)