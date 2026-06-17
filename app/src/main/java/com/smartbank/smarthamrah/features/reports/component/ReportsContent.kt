package com.smartbank.smarthamrah.features.reports.component


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smartbank.smarthamrah.features.reports.ReportsUiState

@Composable
 fun ReportsContent(
    state: ReportsUiState
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {

            SatisfactionCard(
                rate = state.satisfactionRate,
                count = state.satisfactionCount
            )
        }

        item {

            CustomersSection(
                assignedCustomers = state.assignedCustomers,
                activeCustomers = state.activeCustomers
            )
        }

        item {

            InteractionsSection(
                totalMessages = state.totalMessages,
                answeredRequests = state.answeredRequests
            )
        }

        item {

            PerformanceSection(
                averageResponseTime = state.averageResponseTime,
                resolutionTime = state.resolutionTime,
                slaStatus = state.slaStatus,
                proactiveInteractions = state.proactiveInteractions
            )
        }
    }
}