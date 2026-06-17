package com.smartbank.smarthamrah

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.smartbank.smarthamrah.core.navigation.Screens
import com.smartbank.smarthamrah.core.utils.SnackbarType
import com.smartbank.smarthamrah.core.utils.getSnackbarColor
import com.smartbank.smarthamrah.core.utils.showCustomSnackbar
import com.smartbank.smarthamrah.features.home.HomeScreen
import com.smartbank.smarthamrah.features.leaderboard.LeaderBoardScreen
import com.smartbank.smarthamrah.features.reports.ReportsScreen
import android.graphics.Color as AndroidColor
import com.smartbank.smarthamrah.ui.theme.SmartBankTheme
import com.smartbank.smarthamrah.ui.theme.SmartBankTypography
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Add missing import for SnackbarDuration
import androidx.compose.material3.SnackbarDuration
import androidx.core.view.ViewCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.smartbank.smarthamrah.core.chat.ChatSyncManager
import com.smartbank.smarthamrah.core.navigation.Navigator
import kotlinx.coroutines.awaitCancellation
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var chatSyncManager: ChatSyncManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                chatSyncManager.start()

                try {
                    awaitCancellation()
                } finally {
                    chatSyncManager.stop()
                }
            }
        }
        setContent {
            CompositionLocalProvider(
                LocalLayoutDirection provides LayoutDirection.Rtl
            ) {
                SmartBankTheme {
                    Navigator()
                }
            }
        }
//        setContent {
//            SmartBankTheme {
//                Navigator()
//            }
//        }
    }
}