package com.smartbank.smarthamrah.features.reports.component
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartbank.smarthamrah.R
@Composable
fun PerformanceSection(
    averageResponseTime: String,
    resolutionTime: String,
    slaStatus: Int,
    proactiveInteractions: Int,
    modifier: Modifier = Modifier
) {
    BaseReportCard(
        title = "سرعت پاسخگویی و حل مشکل",
        icon = {
            // 🌟 Wrap the Icon in a Box to create the white circular background
            Box(
                modifier = Modifier
                    .size(36.dp) // Total size of the circular background
                    .background(color = Color.White, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.time),
                    contentDescription = null,
                    tint = Color(0xFF1967D2),
                    modifier = Modifier.size(20.dp) // Actual size of the inner icon graphic
                )
            }
        },
        modifier = modifier
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            ReportRow(label = "میانگین زمان پاسخگویی", value = averageResponseTime)
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f)
            )
            ReportRow(label = "زمان رسیدگی تا حل درخواست", value = resolutionTime)
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f)
            )
            // سطر وضعیت SLA همراه با بج ۹۳٪
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "وضعیت SLA پاسخگویی", fontSize = 14.sp, color = Color(0xFF666666))
                    Surface(
                        color = Color(0xFFFFD2D2),
                        shape = CircleShape
                    ) {
                        Text(
                            text = "$slaStatus٪",
                            color = Color(0xFFD93025),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                }
                Text(text = "۳۲ دقیقه", fontSize = 14.sp, fontWeight = FontWeight.Normal, color = Color(0xFF004898))
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f)
            )
            ReportRow(label = "تعاملات پیشگیرانه", value = "$proactiveInteractions مشتری")
        }
    }
}