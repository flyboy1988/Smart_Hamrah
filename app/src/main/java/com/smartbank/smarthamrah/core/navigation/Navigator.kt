package com.smartbank.smarthamrah.core.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.smartbank.smarthamrah.R
import com.smartbank.smarthamrah.core.auth.AuthSessionEvent
import com.smartbank.smarthamrah.core.utils.SnackbarType
import com.smartbank.smarthamrah.core.utils.getSnackbarColor
import com.smartbank.smarthamrah.features.auth.presentation.login.LoginScreen
import com.smartbank.smarthamrah.features.auth.presentation.otp.OtpScreen
import com.smartbank.smarthamrah.features.customers.chat.presentation.CustomerChatScreen
import com.smartbank.smarthamrah.features.customers.chat.presentation.category.ChatCategoryScreen
import com.smartbank.smarthamrah.features.home.HomeScreen
import com.smartbank.smarthamrah.features.profile.ProfileNavigation
import com.smartbank.smarthamrah.features.profile.ProfileScreen
import com.smartbank.smarthamrah.features.profile.ProfileViewModel
import com.smartbank.smarthamrah.features.splash.SplashScreen
import com.smartbank.smarthamrah.features.ticket.FilterScreen
import com.smartbank.smarthamrah.features.ticket.NewTicketScreen
import com.smartbank.smarthamrah.features.ticket.TicketsScreen

@Composable
fun Navigator() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarType by remember { mutableStateOf(SnackbarType.INFO) }

    val navigatorViewModel: NavigatorViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        navigatorViewModel.authSessionManager.events.collect { event ->
            when (event) {
                AuthSessionEvent.SessionExpired -> {
                    navController.navigate(Screens.Login.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0),
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = getSnackbarColor(snackbarType),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(14.dp)
                )
            }
        }
    ) {paddingValues ->

        NavHost(
            navController = navController,
            startDestination = Screens.Splash.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            composable(Screens.Splash.route) {
                SplashScreen(navController = navController)
            }

            composable(Screens.Login.route) {
                LoginScreen(
                    onNavigateToOtp = { username, password ->
                        navController.navigate(
                            Screens.Otp.createRoute(
                                username = username,
                                password = password
                            )
                        )
                    }
                )
            }

            composable(
                route = Screens.Otp.route,
                arguments = listOf(
                    navArgument("username") { type = NavType.StringType },
                    navArgument("password") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                OtpScreen(
                    username = backStackEntry.arguments?.getString("username").orEmpty(),
                    password = backStackEntry.arguments?.getString("password").orEmpty(),
                    onNavigateToHome = {
                        navController.navigate(Screens.Home.route) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Screens.Home.route) {
                HomeScreen(
                    snackbarHostState = snackbarHostState,
                    onSnackbarTypeChange = { snackbarType = it },
                    onNavigateToTickets = {
                        navController.navigate(Screens.Tickets.route)
                    },
                    onNavigateToChatCategory = { bankerName ->
                        navController.navigate(
                            Screens.ChatCategory.createRoute(bankerName)
                        )
                    },
                    onNavigateToCustomerChat = { bankerName, categoryId, categoryTitle ->
                        navController.navigate(
                            Screens.CustomerChat.createRoute(
                                bankerName = bankerName,
                                categoryId = categoryId,
                                categoryTitle = categoryTitle
                            )
                        )
                    },
                    onCallBankerClick = {
                        snackbarType = SnackbarType.INFO
                    }
                )
            }

            composable(Screens.Tickets.route) {
                TicketsScreen(navController = navController)
            }

            composable(Screens.NewTicket.route) {
                ScreenWrapper(
                    title = "تیکت جدید",
                    navController = navController,
                    showBackButton = true
                ) { paddingValues ->
                    NewTicketScreen(
                        modifier = Modifier,
                        navController = navController,
                        onSubmit = { _, _, _, _ ->
                            navController.popBackStack()
                        }
                    )
                }
            }

            composable(Screens.Filter.route) {
                FilterScreen(navController = navController)
            }

            composable(
                route = Screens.ChatCategory.route,
                arguments = listOf(
                    navArgument("bankerName") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val bankerName = backStackEntry.arguments
                    ?.getString("bankerName")
                    .orEmpty()
                    .ifBlank { "بانکدار" }

                ChatCategoryScreen(
                    customerId = null,
                    customerName = bankerName,
                    navController = navController
                )
            }

            composable(
                route = Screens.CustomerChat.route,
                arguments = listOf(
                    navArgument("bankerName") { type = NavType.StringType },
                    navArgument("categoryId") { type = NavType.StringType },
                    navArgument("categoryTitle") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                CustomerChatScreen(
                    customerName = backStackEntry.arguments
                        ?.getString("bankerName")
                        .orEmpty()
                        .ifBlank { "بانکدار" },
                    categoryId = backStackEntry.arguments
                        ?.getString("categoryId")
                        .orEmpty(),
                    categoryTitle = backStackEntry.arguments
                        ?.getString("categoryTitle")
                        .orEmpty(),
                    customerId = null,
                    navController = navController
                )
            }

            composable(Screens.Profile.route) {
                val profileViewModel: ProfileViewModel = hiltViewModel()
                val profileState by profileViewModel.uiState.collectAsState()

                LaunchedEffect(Unit) {
                    profileViewModel.navigation.collect { nav ->
                        when (nav) {
                            ProfileNavigation.Login -> {
                                navController.navigate(Screens.Login.route) {
                                    popUpTo(0) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                }

                ScreenWrapper(
                    title = stringResource(R.string.profile_title),
                    navController = navController,
                    showBackButton = true
                ) {
                    ProfileScreen(
                        uiState = profileState.profile,
                        isLogoutLoading = profileState.isLogoutLoading,
                        onLogoutClick = {
                            profileViewModel.logout()
                        }
                    )
                }
            }
        }
    }
}