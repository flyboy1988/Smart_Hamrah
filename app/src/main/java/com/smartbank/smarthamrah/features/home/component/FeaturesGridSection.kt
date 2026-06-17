package com.smartbank.smarthamrah.features.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FeaturesGridSection() {
    // We group your banking features into pairs for the 2-column grid
    val items = listOf(
        Pair("ثبت تیکت", "۲ تیکت جدید"),
        Pair("گفتگو", "۳ پیام جدید"),
        Pair("تماس", "ماهانه"),
        Pair("دسترسی", "۱۲۴ مشتری"),
        Pair("گزارشات", "ماهانه"),
        Pair("مشتریان", "۱۲۴ مشتری")
    )

    Column(
        modifier = Modifier.padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // This loop takes the list and processes 2 items per Row
        for (i in items.indices step 2) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FeatureCard(
                    title = items[i].first,
                    subtitle = items[i].second,
                    modifier = Modifier.weight(1f)
                )
                // Safety check in case you have an odd number of items
                if (i + 1 < items.size) {
                    FeatureCard(
                        title = items[i + 1].first,
                        subtitle = items[i + 1].second,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}