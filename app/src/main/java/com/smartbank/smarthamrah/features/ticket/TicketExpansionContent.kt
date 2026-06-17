package com.smartbank.smarthamrah.features.ticket

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartbank.smarthamrah.features.ticket.data.Ticket

@Composable
fun TicketExpansionContent(
    ticket: Ticket,
    onReplySubmitted: (String) -> Unit
) {
    var showReplyBottomSheet by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {

        // Inner grey-bordered content capsule box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                .padding(14.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {

                // --- USER TICKET DESCRIPTION SECTION ---
                Text(
                    text = "توضیحات",
                    fontSize = 11.sp,
                    color = Color(0xFF9E9E9E),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Right
                )
                Text(
                    text = ticket.description ?: "",
                    fontSize = 12.sp,
                    color = Color(0xFF1A1A1A),
                    modifier = Modifier
                        .padding(top = 6.dp, bottom = 12.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Right,
                    lineHeight = 22.sp
                )

                // --- TEAM SUPPORT REPLY SECTION ---
                if (ticket.reply != null) {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color(0xFFECEFF1),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {


                        // Right Side inside Box: Label Title
                        Text(
                            text = "پاسخ",
                            fontSize = 11.sp,
                            color = Color(0xFF9E9E9E),
                            textAlign = TextAlign.Right
                        )
                        // Left Side inside Box: Date Text
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = "تاریخ پاسخ:", fontSize = 11.sp, color = Color(0xFF9E9E9E))
                            Text(text = ticket.reply.date, fontSize = 11.sp, color = Color(0xFF9E9E9E))
                        }
                    }

                    Text(
                        text = ticket.reply.message,
                        fontSize = 13.sp,
                        color = Color(0xFF1A1A1A),
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Right,
                        lineHeight = 22.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- LOWER INTERACTION BUTTON ROW ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Button(
                onClick = { showReplyBottomSheet = true },
                modifier = Modifier
                    .height(30.dp)
                    .width(110.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0D47A1),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "پاسخ",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    if (showReplyBottomSheet) {
        TicketReplyBottomSheet(
            onDismiss = { showReplyBottomSheet = false },
            onSubmit = { replyText ->
                onReplySubmitted(replyText)
                showReplyBottomSheet = false
            }
        )
    }
}