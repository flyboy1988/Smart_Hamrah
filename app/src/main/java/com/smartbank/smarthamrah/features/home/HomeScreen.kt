package com.smartbank.smarthamrah.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smartbank.smarthamrah.R
import com.smartbank.smarthamrah.core.utils.SnackbarType
import com.smartbank.smarthamrah.core.utils.showCustomSnackbar

@Composable
fun HomeScreen(
    snackbarHostState: SnackbarHostState,
    onSnackbarTypeChange: (SnackbarType) -> Unit,
    onNavigateToTickets: () -> Unit,
    onNavigateToChatCategory: (bankerName: String) -> Unit,
    onNavigateToCustomerChat: (bankerName: String, categoryId: String, categoryTitle: String) -> Unit,
    onCallBankerClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigation.collect { nav ->
            when (nav) {
                is HomeNavigation.Chat -> {
                    onNavigateToCustomerChat(
                        nav.bankerName,
                        nav.categoryId,
                        nav.categoryTitle
                    )
                }

                is HomeNavigation.ChatCategory -> {
                    onNavigateToChatCategory(nav.bankerName)
                }

                HomeNavigation.Tickets -> {
                    onNavigateToTickets()
                }

                HomeNavigation.BankerProfile -> {

                }
            }
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            onSnackbarTypeChange(SnackbarType.ERROR)
            snackbarHostState.showCustomSnackbar(message)
            viewModel.clearError()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF096BD8))
    ) {
        HomeHeader(
            customerName = uiState.customerProfile.customerName
        )

        HomeContent(
            ticketCount = uiState.customerProfile.callCount,
            isCheckingConversation = uiState.isCheckingConversation,
            onTicketsClick = onNavigateToTickets,
            onChatClick = viewModel::onChatClicked,
            onCallClick = onCallBankerClick
        )
    }
}

@Composable
private fun HomeHeader(
    customerName: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()

            .height(242.dp)
            .background(
                color = Color(0xFF096BD8)

            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(122.dp),
                shape = CircleShape,
                color = Color(0xFFEAF4FF),
                border = androidx.compose.foundation.BorderStroke(
                    width = 2.dp,
                    color = Color(0xFFB7DDFF)
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.customer2),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = customerName,
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "خوش آمدید",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun HomeContent(
    ticketCount: Int,
    isCheckingConversation: Boolean,
    onTicketsClick: () -> Unit,
    onChatClick: () -> Unit,
    onCallClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth().fillMaxHeight()
       ,
        color = Color.White,
        shape = RoundedCornerShape(
            topStart = 28.dp,
            topEnd = 28.dp
        )    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 9.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HomeMenuItem(
                title = "تیکت",
                subtitle = "$ticketCount عدد",
                backgroundColor = Color(0xFFCFF7E2),
                titleColor = Color(0xFF047857),
                iconBackgroundColor = Color(0xFF34D399),
                icon = HomeIcon.Ticket,
                onClick = onTicketsClick
            )

            HomeMenuItem(
                title = "گفتگو",
                subtitle = "",
                backgroundColor = Color(0xFFCFE5FA),
                titleColor = Color(0xFF075985),
                iconBackgroundColor = Color(0xFF4EA3FF),
                icon = HomeIcon.Chat,
                isLoading = isCheckingConversation,
                onClick = onChatClick
            )

            HomeMenuItem(
                title = "تماس",
                subtitle = "09121234532",
                backgroundColor = Color(0xFFFFE9B8),
                titleColor = Color(0xFFEA580C),
                iconBackgroundColor = Color(0xFFFFC95C),
                icon = HomeIcon.Call,
                onClick = onCallClick
            )
        }
    }
}

@Composable
private fun HomeMenuItem(
    title: String,
    subtitle: String,
    backgroundColor: Color,
    titleColor: Color,
    iconBackgroundColor: Color,
    icon: HomeIcon,
    isLoading: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable(enabled = !isLoading, onClick = onClick)
            .padding(horizontal = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {




        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                color = titleColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth()
            )

            if (subtitle.isNotBlank()) {
                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = subtitle,
                    color = titleColor.copy(alpha = 0.75f),
                    fontSize = 10.sp,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Surface(
            modifier = Modifier.size(28.dp),
            shape = CircleShape,
            color = iconBackgroundColor
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Icon(
                        imageVector = when (icon) {
                            HomeIcon.Ticket -> Icons.Outlined.ReceiptLong
                            HomeIcon.Chat -> Icons.Outlined.ChatBubbleOutline
                            HomeIcon.Call -> Icons.Outlined.Call
                        },
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

private enum class HomeIcon {
    Ticket,
    Chat,
    Call
}