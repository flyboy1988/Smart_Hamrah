package com.smartbank.smarthamrah.features.auth.presentation.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun CircularLogo(
    @DrawableRes imageResId: Int, // گرفتن آیدی عکس به عنوان ورودی
    size :Dp=120.dp,
    modifier: Modifier = Modifier,
    paddingInside: Dp = 0.dp // پدینگ پیش‌فرض صفر برای پر کردن کامل
) {
    Box(
        modifier = modifier
            .size(size) // سایز کادر دایره‌ای
            .background(Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = null,
            contentScale = ContentScale.Crop, // تغییر به ContentScale.Crop برای پر کردن کامل فضا
            modifier = Modifier
                .fillMaxSize() // پر کردن تمام فضای Box
                .padding(paddingInside) // اعمال پدینگ (اگر صفر باشد، تأثیری ندارد)
        )
    }
}