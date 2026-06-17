package com.smartbank.smarthamrah.core.navigation

import android.net.Uri

sealed class Screens(val route: String) {
    data object Splash : Screens("splash")
    data object Login : Screens("login")
    data object Home : Screens("home")
    data object Tickets : Screens("tickets")
    data object NewTicket : Screens("new_ticket")
    data object Filter : Screens("filter")
    data object Profile : Screens("profile")

    data object Otp : Screens("otp/{username}/{password}") {
        fun createRoute(username: String, password: String): String =
            "otp/${Uri.encode(username)}/${Uri.encode(password)}"
    }

    data object ChatCategory : Screens("chat_category/{bankerName}") {
        fun createRoute(bankerName: String): String =
            "chat_category/${Uri.encode(bankerName.ifBlank { "بانکدار" })}"

        fun createRoute(customerName: String, customerId: Long): String =
            createRoute(customerName)
    }

    data object CustomerChat : Screens("customer_chat/{bankerName}/{categoryId}/{categoryTitle}") {
        fun createRoute(
            bankerName: String,
            categoryId: String,
            categoryTitle: String
        ): String = "customer_chat/${Uri.encode(bankerName.ifBlank { "بانکدار" })}/${Uri.encode(categoryId)}/${Uri.encode(categoryTitle)}"

        fun createRoute(
            customerName: String,
            categoryId: String,
            categoryTitle: String,
            customerId: Long
        ): String = createRoute(customerName, categoryId, categoryTitle)
    }

    // Kept only for old unused screens that may still compile in source tree.
    data object Leaderboard : Screens("leaderboard")
    data object Reports : Screens("reports")
    data object Customers : Screens("customers")
    data object ReportDetail : Screens("report_detail/{reportId}") { fun createRoute(reportId: String) = "report_detail/${Uri.encode(reportId)}" }
    data object Settings : Screens("settings")
}
