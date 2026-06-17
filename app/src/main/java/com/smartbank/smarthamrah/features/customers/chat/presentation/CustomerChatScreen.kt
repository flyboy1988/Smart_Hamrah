package com.smartbank.smarthamrah.features.customers.chat.presentation

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.smartbank.smarthamrah.R
import com.smartbank.smarthamrah.core.navigation.Screens
import com.smartbank.smarthamrah.core.utils.SnackbarType
import com.smartbank.smarthamrah.core.utils.getSnackbarColor
import com.smartbank.smarthamrah.core.utils.showCustomSnackbar
import com.smartbank.smarthamrah.features.customers.chat.domain.model.ChatMessage
import com.smartbank.smarthamrah.features.customers.chat.domain.model.MessageType
import com.smartbank.smarthamrah.features.customers.chat.domain.model.SenderType
import com.smartbank.smarthamrah.ui.theme.GrayText
import saman.zamani.persiandate.PersianDate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private sealed interface ChatListItem {
    data class DateHeader(
        val key: String,
        val title: String
    ) : ChatListItem

    data class MessageItem(
        val message: ChatMessage
    ) : ChatListItem
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerChatScreen(
    customerName: String,
    categoryId: String,
    categoryTitle: String,
    navController: NavController,
    modifier: Modifier = Modifier,
    customerId: Long? = null,
    viewModel: CustomerChatViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val colors = MaterialTheme.colorScheme
    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()

    var showBottomBar by remember { mutableStateOf(true) }
    var snackbarType by remember { mutableStateOf(SnackbarType.INFO) }
    var showFilterSheet by remember { mutableStateOf(false) }
    val showEndChatButton = false
    var searchQuery by remember { mutableStateOf("") }

    val chatItems = remember(uiState.messages) {
        uiState.messages.toChatListItems()
    }

    LaunchedEffect(categoryId, customerId) {
        categoryId.toLongOrNull()?.let { viewModel.onCategorySelected(it) }
        viewModel.onScreenStarted(customerId = customerId)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onScreenStopped()
        }
    }
    LaunchedEffect(uiState.downloadMessage) {
        uiState.downloadMessage?.let { message ->
            snackbarType = SnackbarType.SUCCESS
            snackbarHostState.showCustomSnackbar(message = message)
        }
    }
    LaunchedEffect(uiState.downloadedFile) {
        val downloadedFile = uiState.downloadedFile ?: return@LaunchedEffect

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(downloadedFile.uri, downloadedFile.mimeType)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        runCatching {
            context.startActivity(
                Intent.createChooser(intent, "باز کردن فایل با")
            )
        }.onFailure {
            Toast.makeText(
                context,
                "برنامه‌ای برای باز کردن این فایل پیدا نشد",
                Toast.LENGTH_SHORT
            ).show()
        }

        viewModel.clearDownloadedFile()
    }
    LaunchedEffect(uiState.ratingSubmitted) {
        if (uiState.ratingSubmitted) {
            navController.navigate(Screens.Home.route) {
                popUpTo(Screens.Home.route) {
                    inclusive = false
                }
                launchSingleTop = true
            }

            viewModel.consumeRatingSubmitted()
        }
    }
    val newestMessageKey = remember(uiState.messages) {
        uiState.messages.firstOrNull()?.id
    }

    LaunchedEffect(newestMessageKey) {
        if (newestMessageKey != null) {
            kotlinx.coroutines.delay(100)
            listState.animateScrollToItem(0)
        }
    }
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarType = SnackbarType.ERROR
            snackbarHostState.showCustomSnackbar(message = message)
            viewModel.clearError()
        }
    }

    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri ?: return@rememberLauncherForActivityResult

        val mimeType = context.contentResolver.getType(uri)

        if (mimeType.isAllowedChatFile()) {
            viewModel.sendFile(
                uri = uri,
                mimeType = mimeType,
                customerId = customerId
            )
        } else {
            Toast.makeText(
                context,
                "فقط عکس، PDF، Word، Doc یا Excel مجاز است",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = getSnackbarColor(snackbarType),
                    contentColor = Color.White
                )
            }
        },
        topBar = {
            CustomerChatTopBar(
                customerName = customerName,
                colors = colors,
                onBackClick = { navController.popBackStack() }
            )
        },
        bottomBar = {
            if (uiState.isConversationClosed) {
                Surface(
                    color = Color.White,
                    shadowElevation = 4.dp
                ) {
                    Text(
                        text = "این گفتگو خاتمه یافته است. لطفاً امتیاز خود را ثبت کنید.",
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(16.dp),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                CustomerChatBottomBar(
                    inputText = uiState.inputText,
                    isSending = uiState.isSending,
                    isUploading = uiState.isUploading,
                    showEndChatButton = showEndChatButton,
                    colors = colors,
                    onInputChanged = viewModel::onInputChanged,
                    onSendClick = { viewModel.sendText(customerId = customerId) },
                    onPickFileClick = {
                        filePicker.launch(allowedChatMimeTypes)
                    },
                    onEndChatConfirmed = {
                        showBottomBar = false
                        snackbarType = SnackbarType.SUCCESS
                    }
                )
            }
        }
    ) { innerPadding ->

        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(colors.surfaceVariant.copy(alpha = 0.2f))
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                SearchBar(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    onMenuClick = { showFilterSheet = true },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )

                CustomerChatHeaderInfo(
                    categoryTitle = categoryTitle,
                    colors = colors
                )

                Spacer(modifier = Modifier.height(4.dp))

                when {
                    uiState.isInitialLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = colors.primary)
                        }
                    }

                    uiState.messages.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.chat_empty_message),
                                modifier = Modifier.padding(horizontal = 24.dp),
                                color = colors.onSurfaceVariant,
                                fontSize = 14.sp
                            )
                        }
                    }

                    else -> {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            reverseLayout = true,
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(
                                items = chatItems.asReversed(),
                                key = { item ->
                                    when (item) {
                                        is ChatListItem.DateHeader -> "date_${item.key}"
                                        is ChatListItem.MessageItem -> "msg_${item.message.id}"
                                    }
                                }
                            ) { item ->
                                when (item) {
                                    is ChatListItem.DateHeader -> {
                                        ChatDateHeader(text = item.title)
                                    }

                                    is ChatListItem.MessageItem -> {
                                        ChatMessageBubble(
                                            message = item.message,
                                            colors = colors
                                            ,
                                            onFileClick = { message ->
                                                viewModel.downloadFile(message.file?.fileId)
                                            }
                                        )

                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (showFilterSheet) {
                CustomerFilterBottomSheet(
                    onDismiss = { showFilterSheet = false },
                    onApplyFilters = { showFilterSheet = false }
                )
            }
        }
        if (uiState.showRatingDialog) {
            RatingDialog(
                isLoading = uiState.isSubmittingRate,
                onSubmit = { rate, comment ->
                    viewModel.submitRating(rate, comment)
                }
            )
        }
    }
}

@Composable
fun RatingDialog(
    isLoading: Boolean,
    onSubmit: (rate: Int, comment: String?) -> Unit
) {
    var selectedRate by remember { mutableIntStateOf(0) }
    var comment by remember { mutableStateOf("") }

    BackHandler(enabled = true) {
    }

    AlertDialog(
        onDismissRequest = {
            // عمداً خالی؛ کلیک بیرون بسته نشود
        },
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = "امتیاز به گفتگو",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(
                    text = "لطفاً برای ادامه، به این گفتگو امتیاز دهید.",
                    fontSize = 14.sp
                )

                Spacer(Modifier.height(16.dp))

                CompositionLocalProvider(
                    LocalLayoutDirection provides LayoutDirection.Ltr
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(5) { index ->
                            val rate = index + 1

                            Image(
                                painter = painterResource(
                                    id = if (rate <= selectedRate) {
                                        R.drawable.star
                                    } else {
                                        R.drawable.star_white
                                    }
                                ),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(34.dp)
                                    .clickable(enabled = !isLoading) {
                                        selectedRate = rate
                                    }
                            )

                            Spacer(modifier = Modifier.width(4.dp))
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5,
                    placeholder = {
                        Text("نظر شما اختیاری است")
                    },
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSubmit(
                        selectedRate,
                        comment.takeIf { it.isNotBlank() }
                    )
                },
                enabled = selectedRate > 0 && !isLoading,
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("ثبت امتیاز")
                }
            }
        },
        dismissButton = {}
    )
}

@Composable
private fun CustomerChatTopBar(
    customerName: String,
    colors: ColorScheme,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(colors.primary)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.avatar),
                    contentDescription = stringResource(R.string.desc_customer_avatar),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, colors.onPrimary, CircleShape)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = customerName,
                    color = colors.onPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 12.dp)
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = stringResource(R.string.desc_close_screen),
                tint = colors.onPrimary,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = onBackClick)
            )
        }
    }
}

@Composable
private fun CustomerChatBottomBar(
    inputText: String,
    isSending: Boolean,
    isUploading: Boolean,
    showEndChatButton: Boolean,
    colors: ColorScheme,
    onInputChanged: (String) -> Unit,
    onSendClick: () -> Unit,
    onPickFileClick: () -> Unit,
    onEndChatConfirmed: () -> Unit
) {
    Surface(
        tonalElevation = 0.dp,
        shadowElevation = 4.dp,
        color = Color.White,
        shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (inputText.isNotBlank()) {
                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = onSendClick,
                    enabled = !isSending,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = colors.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Send,
                        contentDescription = null
                    )
                }
            }

            TextField(
                value = inputText,
                onValueChange = onInputChanged,
                modifier = Modifier.weight(1.5f),
                placeholder = {
                    Text(
                        text = stringResource(R.string.write_message),
                        color = GrayText,
                        fontSize = 14.sp
                    )
                },
                enabled = !isSending && !isUploading,
                maxLines = 4,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            if (inputText.isBlank()) {
                if (isUploading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = colors.primary
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AddCircleOutline,
                        contentDescription = null,
                        tint = androidx.compose.ui.graphics.Color.Gray,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(onClick = onPickFileClick)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
private fun CustomerChatHeaderInfo(
    categoryTitle: String,
    colors: ColorScheme
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = Color(0xFFCBE2F7),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = categoryTitle,
                color = Color(0xFF1967D2),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(
                    horizontal = 12.dp,
                    vertical = 6.dp
                )
            )
        }
    }
}

@Composable
private fun ChatDateHeader(
    text: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = Color.White.copy(alpha = 0.9f),
            shape = RoundedCornerShape(50)
        ) {
            Text(
                text = text,
                color = Color(0xFF6F7378),
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
private fun ChatMessageBubble(
    message: ChatMessage,
    colors: ColorScheme,
    onFileClick: (ChatMessage) -> Unit
) {
    val isUser = message.senderType == SenderType.CUSTOMER
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (isUser) Alignment.CenterStart else Alignment.CenterEnd
    ) {
        when {
            message.messageType == MessageType.IMAGE ||
                    message.messageType == MessageType.PDF ||
                    message.messageType == MessageType.WORD ||
                    message.file != null -> {
                FileAttachmentBubble(
                    message = message,
                    colors = colors,onClick = {
                        onFileClick(message)
                    }
                )
            }

            else -> {
                TextMessageBubble(
                    message = message,
                    colors = colors
                )
            }
        }
    }
}

@Composable
private fun TextMessageBubble(
    message: ChatMessage,
    colors: ColorScheme
) {
    val isUser = message.senderType == SenderType.CUSTOMER

    Surface(
        color = if (isUser) colors.primary else colors.secondaryContainer,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.widthIn(max = 280.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(
                text = message.text.orEmpty(),
                color = if (isUser) colors.onPrimary else colors.onSecondaryContainer,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = message.createdAtUtc.toChatTime(),
                fontSize = 10.sp,
                color = if (isUser) {
                    colors.onPrimary.copy(alpha = 0.65f)
                } else {
                    colors.onSurfaceVariant.copy(alpha = 0.55f)
                },
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
fun FileAttachmentBubble(
    message: ChatMessage,
    colors: ColorScheme,
    onClick: () -> Unit
) {
    val isUser = message.senderType == SenderType.CUSTOMER
    val file = message.file
    val fileName = file?.fileName ?: message.text.orEmpty()
    val mimeType = file?.mimeType.orEmpty()

    Surface(
        color = if (isUser) colors.primary else colors.secondaryContainer,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.widthIn(max = 290.dp)
        .clickable { onClick() }
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(
                                color = if (isUser) {
                                    colors.onPrimary.copy(alpha = 0.2f)
                                } else {
                                    colors.primary
                                },
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.docupload),
                            contentDescription = null,
                            tint = colors.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = fileName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isUser) {
                                colors.onPrimary
                            } else {
                                colors.onSecondaryContainer
                            },
                            maxLines = 2
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = mimeType.toFileTypeLabel(),
                            fontSize = 11.sp,
                            color = if (isUser) {
                                colors.onPrimary.copy(alpha = 0.75f)
                            } else {
                                colors.onSecondaryContainer.copy(alpha = 0.75f)
                            }
                        )

                        file?.fileSize?.let { size ->
                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = formatFileSize(size),
                                fontSize = 11.sp,
                                color = if (isUser) {
                                    colors.onPrimary.copy(alpha = 0.7f)
                                } else {
                                    colors.onSecondaryContainer.copy(alpha = 0.7f)
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = message.createdAtUtc.toChatTime(),
                    fontSize = 11.sp,
                    color = if (isUser) {
                        colors.onPrimary.copy(alpha = 0.65f)
                    } else {
                        colors.onSurfaceVariant.copy(alpha = 0.6f)
                    },
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }

    }
}

private fun List<ChatMessage>.toChatListItems(): List<ChatListItem> {
    val result = mutableListOf<ChatListItem>()
    var lastDateKey: String? = null

    sortedBy { parseServerDate(it.createdAtUtc)?.time ?: 0L }
        .forEach { message ->

            val dateKey = message.createdAtUtc.toPersianDateKey()
            val dateTitle = message.createdAtUtc.toPersianChatDate()

            if (
                dateKey.isNotBlank() &&
                dateTitle.isNotBlank() &&
                dateKey != lastDateKey
            ) {
                result += ChatListItem.DateHeader(
                    key = dateKey,
                    title = dateTitle
                )
                lastDateKey = dateKey
            }

            result += ChatListItem.MessageItem(message)
        }

    return result
}

private fun String.toChatTime(): String {
    val date = parseServerDate(this) ?: return ""

    val formatter = SimpleDateFormat("HH:mm", Locale.US)

    return formatter.format(date).toPersianDigits()
}

private fun String.toPersianChatDate(): String {
    val date = parseServerDate(this) ?: return ""

    return try {
        val persianDate = PersianDate(date.time)
        "${persianDate.shYear} ، ${persianDate.shDay} ${persianDate.monthName()}".toPersianDigits()
    } catch (e: Exception) {
        ""
    }
}

private fun String.toPersianDateKey(): String {
    val date = parseServerDate(this) ?: return ""

    return try {
        val persianDate = PersianDate(date.time)
        "${persianDate.shYear}-${persianDate.shMonth}-${persianDate.shDay}"
    } catch (e: Exception) {
        ""
    }
}

private fun parseServerDate(value: String): Date? {
    val normalized = value.replace(
        Regex("\\.(\\d{3})\\d*Z$"),
        ".$1Z"
    )

    val patterns = listOf(
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        "yyyy-MM-dd'T'HH:mm:ss'Z"
    )

    return patterns.firstNotNullOfOrNull { pattern ->
        runCatching {
            SimpleDateFormat(pattern, Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }.parse(normalized)
        }.getOrNull()
    }
}

private fun String.toPersianDigits(): String {
    return this
        .replace('0', '۰')
        .replace('1', '۱')
        .replace('2', '۲')
        .replace('3', '۳')
        .replace('4', '۴')
        .replace('5', '۵')
        .replace('6', '۶')
        .replace('7', '۷')
        .replace('8', '۸')
        .replace('9', '۹')
}

private fun formatFileSize(size: Long): String {
    val kb = size / 1024.0
    val mb = kb / 1024.0

    return if (mb >= 1) {
        "%.1f MB".format(mb)
    } else {
        "%.0f KB".format(kb)
    }
}

private fun String?.isAllowedChatFile(): Boolean {
    return this in allowedChatMimeTypes
}

private val allowedChatMimeTypes = arrayOf(
    "image/jpeg",
    "image/png",
    "application/pdf",
    "application/msword",
    "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
    "application/vnd.ms-excel",
    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
)

private fun String.toFileTypeLabel(): String {
    return when (this) {
        "image/jpeg", "image/png" -> "تصویر"
        "application/pdf" -> "PDF"
        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> "Word"

        "application/vnd.ms-excel",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" -> "Excel"

        else -> "فایل"
    }
}