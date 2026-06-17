package com.smartbank.smarthamrah.features.reports


data class ReportsUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,

    val satisfactionRate: Float = 0f,
    val satisfactionCount: Int = 0,

    val assignedCustomers: Int = 0,
    val activeCustomers: Int = 0,

    val totalMessages: Int = 0,
    val answeredRequests: Int = 0,

    val averageResponseTime: String = "",
    val resolutionTime: String = "",
    val slaStatus: Int = 0,
    val proactiveInteractions: Int = 0,

    val error: String? = null
)