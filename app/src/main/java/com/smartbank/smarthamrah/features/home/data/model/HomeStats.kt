package com.smartbank.smarthamrah.features.home.data.model

data class HomeStats(
    val newTickets: Int,
    val monthlyContacts: Int,
    val monthlyReports: Int,
    val requestCount: Int = 4
)