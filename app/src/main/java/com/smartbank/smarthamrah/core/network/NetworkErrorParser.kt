package com.smartbank.smarthamrah.core.network

import com.google.gson.Gson
import com.smartbank.smarthamrah.core.network.model.ErrorResponse
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

fun Throwable.toReadableMessage(): String {
    return when (this) {
        is HttpException -> parseHttpError()
        is SocketTimeoutException -> "زمان پاسخ‌گویی سرور به پایان رسید. دوباره تلاش کنید."
        is IOException -> "ارتباط با سرور برقرار نشد."
        else -> localizedMessage ?: "خطای ناشناخته رخ داد."
    }
}

private fun HttpException.parseHttpError(): String {
    return try {
        val errorJson = response()?.errorBody()?.string()
        val error = Gson().fromJson(errorJson, ErrorResponse::class.java)
        error.message.ifBlank { "خطا در ارتباط با سرور" }
    } catch (_: Exception) {
        message()
            ?: "خطا در ارتباط با سرور"
    }
}
