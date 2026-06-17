package com.smartbank.smarthamrah.features.reports.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartbank.smarthamrah.R

@Composable
fun SatisfactionCard(
    rate: Float,
    count: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color(0xFF0066DA), //  تغییر رنگ به آبی کاربنی دقیقاً مطابق تصویر دوم
        shape = RoundedCornerShape(20.dp)
    ) {
        // اجبار به سیستم RTL برای قرارگیری درست ستاره در راست و متن در چپ
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center // تراز شدن کل محتوا در مرکز کارت
            ) {

                // ۱. سمت راست: آیکون ستاره زرد بزرگ
                Icon(
                    painter = painterResource(id = R.drawable.stars),
                    contentDescription = null,
                    tint = Color(0xFFFFD000),
                    modifier = Modifier
                        .size(64.dp)
                )

                Spacer(modifier = Modifier.width(20.dp))

                // ۲. سمت چپ: ستون حاوی دو ردیف متن کاملاً راست‌چین و متمرکز
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "رضایت مشتری",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "$rate از $count نظر",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}