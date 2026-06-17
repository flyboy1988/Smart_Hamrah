package com.smartbank.smarthamrah.features.reports.component
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.smartbank.smarthamrah.R

@Composable
fun InteractionsSection(
    totalMessages: Int,
    answeredRequests: Int,
    modifier: Modifier = Modifier
) {
    BaseReportCard(
        title = "تعاملات",
        icon = {
            // 🌟 Wrap the Icon in a Box to create the white circular background
            Box(
                modifier = Modifier
                    .size(36.dp) // Total size of the circular background
                    .background(color = Color.White, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.list),
                    contentDescription = null,
                    tint = Color(0xFF1967D2),
                    modifier = Modifier.size(20.dp) // Actual size of the inner icon graphic
                )
            }
        },
        modifier = modifier
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            ReportRow(label = "کل پیام‌های ردوبدل شده", value = "$totalMessages درخواست")
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f)
            )
            ReportRow(label = "درخواست‌های پاسخ داده شده", value = "$answeredRequests درخواست")
        }
    }
}