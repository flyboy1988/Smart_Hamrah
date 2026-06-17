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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.SentimentSatisfied
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
fun CustomersSection(
    assignedCustomers: Int,
    activeCustomers: Int,
    modifier: Modifier = Modifier
) {
    BaseReportCard(
        title = "مشتریان",
        icon = {

            Box(
                modifier = Modifier
                    .size(36.dp) // Total size of the circular background
                    .background(color = Color.White, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = null,
                    tint = Color(0xFF1967D2),
                    modifier = Modifier.size(20.dp) // Actual size of the inner icon graphic
                )
            }
        },
        modifier = modifier
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            ReportRow(label = "مشتریان اختصاص یافته", value = "$assignedCustomers مشتری")
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "مشتریان فعال", fontSize = 14.sp, color = Color(0xFF666666))
                    // نشان درصد صورتی مطابق تصویر
                    Surface(
                        color = Color(0xFFFFD2D2),
                        shape = CircleShape
                    ) {
                        Text(
                            text = "۹۳٪",
                            color = Color(0xFFD93025),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                    Text(text = "$activeCustomers مشتری", fontSize = 14.sp, fontWeight = FontWeight.Normal, color = Color(0xFF004898))
            }
        }
    }
}