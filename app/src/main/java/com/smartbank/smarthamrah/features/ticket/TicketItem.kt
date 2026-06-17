package com.smartbank.smarthamrah.features.ticket

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartbank.smarthamrah.R
import com.smartbank.smarthamrah.features.ticket.data.Ticket
import com.smartbank.smarthamrah.features.ticket.data.TicketStatus


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketItem(ticket: Ticket, onExpandClick: () -> Unit,
               onReplySubmitted: (String) -> Unit) {
    val colors = MaterialTheme.colorScheme

    val (statusBg, statusTextColor, statusText) = when (ticket.status) {
        TicketStatus.PENDING -> Triple(
            colors.primary.copy(alpha = 0.1f),
            colors.primary,
            "در حال بررسی"
        )
        TicketStatus.CLOSED -> Triple(
            colors.secondary.copy(alpha = 0.1f),
            colors.secondary,
            "خاتمه یافته"
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        onClick = onExpandClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // ردیف اول: عنوان تیکت (چپ) و زمان ارسال (راست)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // سمت چپ: عنوان تیکت
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = stringResource(R.string.ticket_title),
                        fontSize = 11.sp,
                        color = colors.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = ticket.title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface
                    )
                }

                // سمت راست: زمان ارسال
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(R.string.ticket_time),
                        fontSize = 11.sp,
                        color = colors.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = ticket.date+" | "+ticket.time,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ردیف دوم: وضعیت (چپ) و ضمیمه (راست)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // سمت چپ: وضعیت
                // سمت راست: ضمیمه
                if (ticket.hasAttachment) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "سند آپلود شده",
                            fontSize = 11.sp,
                            color = colors.onSurface.copy(alpha = 0.6f)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.attach),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = colors.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
                else{
                    Text(
                        text = "",
                        fontSize = 11.sp,
                        color = colors.onSurface.copy(alpha = 0.6f)
                    )
                }

                Surface(
                    color = statusBg,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = statusText,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = statusTextColor
                    )
                }


            }

            // بخش باز شده (expansion panel)
            if (ticket.isExpanded) {
                TicketExpansionContent(ticket,
                    onReplySubmitted = onReplySubmitted)
            }
        }
    }
}