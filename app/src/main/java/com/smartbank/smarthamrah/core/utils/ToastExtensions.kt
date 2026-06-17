package com.smartbank.smarthamrah.core.utils

import android.content.Context
import android.widget.Toast
import com.droidman.ktoasty.KToasty

enum class ToastType {
    SUCCESS, ERROR, WARNING, INFO
}

fun Context.showToast(message: String, type: ToastType = ToastType.INFO) {
    when (type) {
        ToastType.SUCCESS -> KToasty.success(this, message, Toast.LENGTH_SHORT,withIcon = false).show()
        ToastType.ERROR -> KToasty.error(this, message, Toast.LENGTH_SHORT,withIcon = false).show()
        ToastType.WARNING -> KToasty.warning(this, message, Toast.LENGTH_SHORT,withIcon = false).show()
        ToastType.INFO -> KToasty.info(this, message, Toast.LENGTH_SHORT,withIcon = false).show()
    }
}