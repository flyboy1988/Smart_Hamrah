package com.smartbank.smarthamrah.core.navigation



import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HandleExit(snackbarHostState: SnackbarHostState) {
    val activity = LocalContext.current as? Activity
    var firstBackPressTime by remember { mutableStateOf(0L) }
    val scope = rememberCoroutineScope()

    BackHandler {
        val currentTime = System.currentTimeMillis()

        // اگر فاصله دو بار فشردن دکمه کمتر از ۲ ثانیه باشد، برنامه بسته می‌شود
        if (currentTime - firstBackPressTime < 2000) {
            activity?.finish()
        } else {
            firstBackPressTime = currentTime
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "برای خروج دوباره بازگشت را انتخاب کنید",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }
}