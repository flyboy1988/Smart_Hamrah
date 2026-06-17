package com.smartbank.smarthamrah.features.splash

import android.content.Context
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.smartbank.smarthamrah.R
import com.smartbank.smarthamrah.core.navigation.Screens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val alpha = remember {
        Animatable(0f)
    }

    LaunchedEffect(Unit) {

        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(600)
        )

        delay(2000)

        viewModel.checkLogin()
    }

    LaunchedEffect(Unit) {
        viewModel.navigation.collect { destination ->

            when (destination) {

                SplashNavigation.Home -> {
                    navController.navigate(Screens.Home.route) {
                        popUpTo(Screens.Splash.route) {
                            inclusive = true
                        }
                    }
                }

                SplashNavigation.Login -> {
                    navController.navigate(Screens.Login.route) {
                        popUpTo(Screens.Splash.route) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A67D8))
    ) {

        Image(
            painter = painterResource(id = R.drawable.tejarat),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 110.dp)
                .width(140.dp)
                .alpha(alpha.value)
        )

        Image(
            painter = painterResource(id = R.drawable.logo_tej),
            contentDescription = null,
            colorFilter = ColorFilter.tint(
                Color.White
            ),
            modifier = Modifier
                .align(Alignment.Center)
                .size(150.dp)
                .alpha(alpha.value)
        )
        val versionText = "\u200E${context.getAppVersionName()}"
        Text(
            text = "نسخه ${versionText}",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 90.dp)
                .alpha(alpha.value)
        )
    }
}

private fun Context.getAppVersionName(): String {
    return try {
        packageManager
            .getPackageInfo(packageName, 0)
            .versionName ?: "1.0"
    } catch (e: Exception) {
        "1.0"
    }
}