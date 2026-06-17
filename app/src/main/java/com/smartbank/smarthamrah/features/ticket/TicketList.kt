package com.smartbank.smarthamrah.features.ticket

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.smartbank.smarthamrah.R
import com.smartbank.smarthamrah.core.navigation.Screens
import com.smartbank.smarthamrah.features.ticket.data.Ticket
import com.smartbank.smarthamrah.ui.theme.BrandTeal

@Composable
fun TicketList(navController: NavController,
    tickets: List<Ticket>,
    onExpandClick: (Int) -> Unit,
    onReplySubmitted: (Int, String) -> Unit  // Add this parameter (ticketId, replyText)
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp,
            bottom = 120.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Surface(
                    onClick = {
                        navController.navigate(Screens.Filter.route)
                    },
                    shape = RoundedCornerShape(10.dp),
                    color = BrandTeal
                ) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 8.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.filter),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = stringResource(R.string.filter_text),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        items(
            items = tickets,
            key = { it.id }
        ) { ticket ->
            TicketItem(
                ticket = ticket,
                onExpandClick = {
                    onExpandClick(ticket.id)
                },
                onReplySubmitted = { replyText ->
                    onReplySubmitted(ticket.id, replyText)  // Pass ticket ID with reply
                }
            )
        }
    }
}