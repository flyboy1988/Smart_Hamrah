package com.smartbank.smarthamrah.features.reports

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.smartbank.smarthamrah.R
import com.smartbank.smarthamrah.core.navigation.ScreenWrapper
import com.smartbank.smarthamrah.features.reports.component.ReportsContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    navController: NavController,
    viewModel: ReportsViewModel = viewModel()
) {

    val state by viewModel.uiState.collectAsState()

    val pullRefreshState = rememberPullToRefreshState()

    ScreenWrapper(
        title = stringResource(R.string.feature_reports),
        navController = navController
    ) { modifier ->

        Box(
            modifier = modifier.fillMaxSize()
        ) {

            PullToRefreshBox(
                state = pullRefreshState,
                isRefreshing = state.isRefreshing,
                onRefresh = {
                    viewModel.refresh()
                }
            ) {

                when {

                    state.isLoading -> {

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    else -> {
                        ReportsContent(
                            state = state
                        )
                    }
                }
            }
        }
    }
}