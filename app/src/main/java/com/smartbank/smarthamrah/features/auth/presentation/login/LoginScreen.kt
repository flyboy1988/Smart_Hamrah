package com.smartbank.smarthamrah.features.auth.presentation.login

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.smartbank.smarthamrah.R
import com.smartbank.smarthamrah.core.utils.SnackbarType
import com.smartbank.smarthamrah.core.utils.getSnackbarColor
import com.smartbank.smarthamrah.core.utils.showCustomSnackbar

@Composable
fun LoginScreen(
    onNavigateToOtp: (username: String, password: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()
    val passwordFocusRequester = remember { FocusRequester() }

    val isButtonEnabled =
        uiState.username.isNotBlank() &&
                uiState.password.isNotBlank() &&
                !uiState.isLoading

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showCustomSnackbar(it)
        }
    }

    LaunchedEffect(uiState.navigateToOtp) {
        if (uiState.navigateToOtp) {
            onNavigateToOtp(uiState.username.trim(), uiState.password)
            viewModel.consumeNavigation()
        }
    }

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
                        .padding(horizontal = 16.dp)
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(120.dp))

                    Image(
                        painter = painterResource(id = R.drawable.logo_tej),

                        colorFilter = ColorFilter.tint(
                            Color.White
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(48.dp)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "ورود به حساب کاربری",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "اطلاعات خود را وارد کنید.",
                        color = Color(0xFF0064D4),
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(36.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        color = Color.White,
                        shadowElevation = 1.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(
                                horizontal = 14.dp,
                                vertical = 12.dp
                            )
                        ) {
                            Text(
                                text = "نام کاربری",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Right,
                                color = Color(0xFF3B4554),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            CompositionLocalProvider(
                                LocalLayoutDirection provides LayoutDirection.Rtl
                            ) {
                                OutlinedTextField(
                                    value = uiState.username,
                                    onValueChange = viewModel::onUsernameChanged,
                                    enabled = !uiState.isLoading,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp),
                                    singleLine = true,
                                    placeholder = {
                                        Text(
                                            text = "نام کاربری",
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Right,
                                            fontSize = 11.sp,
                                            color = Color(0xFFB0B0B0)
                                        )
                                    },
                                    textStyle = LocalTextStyle.current.copy(
                                        textAlign = TextAlign.Right
                                    ),
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Next
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onNext = {
                                            passwordFocusRequester.requestFocus()
                                        }
                                    ),
                                    shape = RoundedCornerShape(6.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFFB6B7BD),
                                        unfocusedBorderColor = Color(0xFFB6B7BD),
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White,
                                        disabledContainerColor = Color.White,
                                        cursorColor = Color(0xFF0064D4)
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "رمز عبور",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Right,
                                color = Color(0xFF3B4554),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            CompositionLocalProvider(
                                LocalLayoutDirection provides LayoutDirection.Rtl
                            ) {
                                OutlinedTextField(
                                    value = uiState.password,
                                    onValueChange = viewModel::onPasswordChanged,
                                    enabled = !uiState.isLoading,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp)
                                        .focusRequester(passwordFocusRequester),
                                    singleLine = true,
                                    trailingIcon = {
                                        IconButton(
                                            onClick = viewModel::togglePasswordVisibility
                                        ) {
                                            Icon(
                                                imageVector = if (uiState.isPasswordVisible)
                                                    Icons.Outlined.VisibilityOff
                                                else
                                                    Icons.Outlined.Visibility,
                                                contentDescription = null,
                                                tint = Color(0xFF0064D4)
                                            )
                                        }
                                    },
                                    visualTransformation = if (uiState.isPasswordVisible) {
                                        VisualTransformation.None
                                    } else {
                                        PasswordVisualTransformation()
                                    },
                                    placeholder = {
                                        Text(
                                            text = "رمز عبور",
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Right,
                                            fontSize = 11.sp,
                                            color = Color(0xFFB0B0B0)
                                        )
                                    },
                                    textStyle = LocalTextStyle.current.copy(
                                        textAlign = TextAlign.Right
                                    ),
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Password,
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            if (isButtonEnabled) {
                                                viewModel.login()
                                            }
                                        }
                                    ),
                                    shape = RoundedCornerShape(6.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFFB6B7BD),
                                        unfocusedBorderColor = Color(0xFFB6B7BD),
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White,
                                        disabledContainerColor = Color.White,
                                        cursorColor = Color(0xFF0064D4)
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "فراموشی نام کاربری / رمز عبور",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Right,
                                color = Color(0xFF2541B2),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = viewModel::login,
                        enabled = isButtonEnabled,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(28.dp),
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
                                text = "ادامه",
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

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:1554")
                                }

                                context.startActivity(intent)
                            },
                        contentAlignment = Alignment.Center
                    ) {
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