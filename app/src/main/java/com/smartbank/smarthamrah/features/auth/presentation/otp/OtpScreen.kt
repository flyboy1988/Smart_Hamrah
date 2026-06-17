package com.smartbank.smarthamrah.features.auth.presentation.otp

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.smartbank.smarthamrah.R
import com.smartbank.smarthamrah.core.utils.SnackbarType
import com.smartbank.smarthamrah.core.utils.getSnackbarColor
import com.smartbank.smarthamrah.core.utils.showCustomSnackbar
import com.smartbank.smarthamrah.features.auth.presentation.component.CircularLogo
import kotlinx.coroutines.delay

@Composable
fun OtpScreen(
    username: String,
    password: String,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OtpViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var secondsLeft by remember { mutableIntStateOf(43) }
    var timerKey by remember { mutableIntStateOf(0) }

    LaunchedEffect(timerKey) {
        secondsLeft = 43
        while (secondsLeft > 0) {
            delay(1000)
            secondsLeft--
        }
    }

    LaunchedEffect(uiState.otp) {
        if (uiState.otp.length == 6 && !uiState.isLoading) {
            viewModel.verifyOtp(username)
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showCustomSnackbar(it)
        }
    }

    LaunchedEffect(uiState.navigateToHome) {
        if (uiState.navigateToHome) {
            onNavigateToHome()
            viewModel.consumeNavigation()
        }
    }

    val isOtpComplete = uiState.otp.length == 6

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(snackbarHostState) { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = getSnackbarColor(SnackbarType.ERROR),
                        contentColor = Color.White
                    )
                }
            }
        ) { innerPadding ->

            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF0875E8),
                                Color(0xFF76AEE7),
                                Color(0xFFF7F7F7),
                                Color(0xFFFFFFFF)
                            )
                        )
                    )
                    .imePadding()
                    .navigationBarsPadding()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(120.dp))

                    Image(
                        painter = painterResource(id = R.drawable.shield),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(48.dp),
                    )


                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "کد تایید",
                        color = Color.White,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "لطفا کد تایید پیامک شده را وارد نمایید",
                        color = Color(0xFF0C66C9),
                        fontSize = 12.sp,
                        lineHeight = 20.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(34.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        color = Color.White,
                        shadowElevation = 1.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(
                                horizontal = 14.dp,
                                vertical = 14.dp
                            )
                        ) {
                            Text(
                                text = "کد را وارد کنید",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Right,
                                color = Color(0xFF111111),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            OtpCodeFields(
                                otp = uiState.otp,
                                enabled = !uiState.isLoading,
                                isError = uiState.errorMessage != null,
                                onOtpChange = viewModel::onOtpChanged
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextButton(
                                    enabled = secondsLeft == 0 && !uiState.isLoading,
                                    onClick = {
                                        viewModel.resendOtp(
                                            username = username,
                                            password = password
                                        )
                                        timerKey++
                                    },
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text(
                                        text = if (secondsLeft == 0) {
                                            "ارسال مجدد کد"
                                        } else {
                                            "ارسال مجدد کد بعد از ${formatTimer(secondsLeft)}"
                                        },
                                        fontSize = 10.sp,
                                        color = if (secondsLeft == 0) {
                                            Color(0xFF2F80ED)
                                        } else {
                                            Color(0xFF9AA3AF)
                                        }
                                    )
                                }

                                if (uiState.errorMessage != null) {
                                    Text(
                                        text = "کد وارد شده اشتباه است",
                                        color = Color(0xFFE53935),
                                        fontSize = 10.sp
                                    )
                                } else {
                                    Text(
                                        text = "",
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            viewModel.verifyOtp(username)
                        },
                        enabled = isOtpComplete && !uiState.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0064D4),
                            contentColor = Color.White,
                            disabledContainerColor = Color(0xFFB7C0CF),
                            disabledContentColor = Color.White
                        )
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "تایید",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .padding(horizontal = 18.dp, vertical = 18.dp)
                        .statusBarsPadding(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.tejarat),
                        contentDescription = null,
                        modifier = Modifier.width(94.dp)
                    )

                    Surface(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:1554")
                            }
                            context.startActivity(intent)
                        },
                        modifier = Modifier.size(44.dp),
                        color = Color.Transparent,
                        shape = CircleShape
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                painter = painterResource(id = R.drawable.support),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OtpCodeFields(
    otp: String,
    enabled: Boolean,
    isError: Boolean,
    onOtpChange: (String) -> Unit
) {
    val otpLength = 6
    val itemSize = 42.dp

    val focusRequesters = remember {
        List(otpLength) { FocusRequester() }
    }

    LaunchedEffect(Unit) {
        focusRequesters.first().requestFocus()
    }

    fun buildOtp(index: Int, value: Char?): String {
        val chars = CharArray(otpLength) { i ->
            otp.getOrNull(i) ?: ' '
        }

        chars[index] = value ?: ' '

        return chars
            .concatToString()
            .replace(" ", "")
            .take(otpLength)
    }

    fun setCharAt(index: Int, value: Char?) {
        onOtpChange(buildOtp(index, value))
    }

    fun moveNext(index: Int) {
        if (index < otpLength - 1) {
            focusRequesters[index + 1].requestFocus()
        }
    }

    fun movePrevious(index: Int) {
        if (index > 0) {
            focusRequesters[index - 1].requestFocus()
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(otpLength) { index ->

                val currentChar = otp.getOrNull(index)?.toString().orEmpty()

                BasicTextField(
                    value = currentChar,
                    onValueChange = { value ->
                        if (!enabled) return@BasicTextField

                        when {
                            value.isEmpty() -> {
                                setCharAt(index, null)
                            }

                            value.any { it.isDigit() } -> {
                                val digit = value
                                    .filter { it.isDigit() }
                                    .takeLast(1)
                                    .first()

                                setCharAt(index, digit)
                                moveNext(index)
                            }
                        }
                    },
                    enabled = enabled,
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        color = Color(0xFF111111),
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword
                    ),
                    modifier = Modifier
                        .size(itemSize)
                        .background(Color.White, CircleShape)
                        .border(
                            width = 1.dp,
                            color = if (isError) Color(0xFFE53935) else Color(0xFFE9E9E9),
                            shape = CircleShape
                        )
                        .focusRequester(focusRequesters[index])
                        .onKeyEvent { event ->
                            if (
                                event.type == KeyEventType.KeyDown &&
                                event.key == Key.Backspace
                            ) {
                                if (otp.getOrNull(index) != null) {
                                    setCharAt(index, null)
                                } else {
                                    movePrevious(index)
                                }
                                true
                            } else {
                                false
                            }
                        },
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            innerTextField()
                        }
                    }
                )
            }
        }
    }
}

private fun formatTimer(seconds: Int): String {
    return "۰۰:${seconds.toString().padStart(2, '0')}".toPersianDigits()
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