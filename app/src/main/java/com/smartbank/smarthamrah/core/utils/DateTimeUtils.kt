package com.smartbank.smarthamrah.core.utils
import android.os.Build
import androidx.annotation.RequiresApi
import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@RequiresApi(Build.VERSION_CODES.O)
fun String.toPersianChatDate(): String {
    return try {

        val date = OffsetDateTime.parse(this)

        val persianDate = PersianDate(
            java.util.Date.from(date.toInstant())
        )

        "${persianDate.shYear}، ${persianDate.shDay} ${persianDate.monthName}"
    } catch (e: Exception) {
        ""
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.toChatTime(): String {
    return try {
        OffsetDateTime
            .parse(this)
            .atZoneSameInstant(ZoneId.systemDefault())
            .format(
                DateTimeFormatter.ofPattern("HH:mm")
            )
    } catch (e: Exception) {
        ""
    }
}



// تاریخ را به فرمت فارسی برای هدر روز چت برمی‌گرداند

// تبدیل اعداد لاتین به فارسی
fun String.toPersianDigits(): String {
    return this
        .replace('0', '۰')
        .replace('1', '۱')
        .replace('2', '۲')
        .replace('3', '۳')
        .replace('4', '۴')
        .replace('5', '۵')
        .replace('6', '۶')
        .replace('7', '۷')
        .replace('8', '۸')
        .replace('9', '۹')
}


// تبدیل رشته تاریخ ISO 8601 سرور به Date
private fun parseServerDate(value: String): Date? {
    val normalized = value.replace(Regex("\\.(\\d{3})\\d*Z$"), ".$1Z")
    val patterns = listOf(
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        "yyyy-MM-dd'T'HH:mm:ss'Z'"
    )
    return patterns.firstNotNullOfOrNull { pattern ->
        runCatching {
            SimpleDateFormat(pattern, Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }.parse(normalized)
        }.getOrNull()
    }
}

// کلید یکتا برای گروه‌بندی پیام‌ها بر اساس روز شمسی
fun String.toPersianDateKey(): String {
    val date = parseServerDate(this) ?: return ""
    val persianDate = PersianDate(date.time)
    return "${persianDate.shYear}-${persianDate.shMonth}-${persianDate.shDay}"
}

fun getPersianTimeString(dateString: String): String {
    val pd = parseIsoDateToPersian(dateString)
    val format = PersianDateFormat("HH:mm")
    return format.format(pd)
}

fun parseIsoDateToPersian(dateString: String): PersianDate {
    // استفاده از فرمت استاندارد ISO 8601 با میلی ثانیه (SSS)
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    val date = try {
        sdf.parse(dateString)
    } catch (e: Exception) {
        Date()
    }

    return PersianDate(date?.time ?: System.currentTimeMillis())
}