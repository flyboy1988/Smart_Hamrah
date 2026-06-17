package com.smartbank.smarthamrah.core.network

sealed interface ResultWrapper<out T> {

    data class Success<T>(
        val data: T
    ) : ResultWrapper<T>

    data class Error(
        val code: Int,
        val message: String
    ) : ResultWrapper<Nothing>
}