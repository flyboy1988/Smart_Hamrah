package com.smartbank.smarthamrah.core.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.util.Locale
object FileUtil{


fun getFileSizeFormatted(context: Context, uri: Uri): String {
    var bytes: Long = 0
    context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
        if (cursor.moveToFirst() && !cursor.isNull(sizeIndex)) {
            bytes = cursor.getLong(sizeIndex)
        }
    }

    if (bytes <= 0) return "۰ بایت"

    // Calculate size in KB or MB
    val sizeInKb = bytes / 1024.0
    val resultString = if (sizeInKb >= 1024) {
        String.format(Locale.US, "%.1f مگابایت", sizeInKb / 1024.0)
    } else {
        String.format(Locale.US, "%.0f کیلوبایت", sizeInKb)
    }

    // Convert English numbers to Persian digits dynamically
    return resultString
        .replace("0", "۰")
        .replace("1", "۱")
        .replace("2", "۲")
        .replace("3", "۳")
        .replace("4", "۴")
        .replace("5", "۵")
        .replace("6", "۶")
        .replace("7", "۷")
        .replace("8", "۸")
        .replace("9", "۹")
        .replace(".", "٫") // Decimal point decoration
}
}