package com.smartbank.smarthamrah.features.customers.chat.presentation.category

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.smartbank.smarthamrah.R
import com.smartbank.smarthamrah.core.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatCategoryScreen(
    customerId: Long?,
    customerName: String,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ChatCategoryViewModel = hiltViewModel()
) {
    val colors = MaterialTheme.colorScheme
    val uiState by viewModel.uiState.collectAsState()

    var isDropdownExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadCategories()
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            topBar = {
                ChatCategoryTopBar(
                    customerName = customerName,
                    onBackClick = { navController.popBackStack() }
                )
            }
        ) { innerPadding ->
            PullToRefreshBox(
                isRefreshing = uiState.isRefreshing,
                onRefresh = {
                    isDropdownExpanded = false
                    viewModel.refreshCategories()
                },
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(colors.surfaceVariant.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        item {
                            CategoryDropdownSection(
                                isDropdownExpanded = isDropdownExpanded,
                                onDropdownExpandedChange = {
                                    isDropdownExpanded = it
                                },
                                uiState = uiState,
                                onCategorySelected = { category ->
                                    viewModel.selectCategory(category)
                                    isDropdownExpanded = false
                                }
                            )
                        }
                    }

                    Button(
                        onClick = {
                            val selected = uiState.selectedCategory ?: return@Button

                            navController.navigate(
                                Screens.CustomerChat.createRoute(
                                    bankerName = customerName,
                                    categoryId = selected.id.toString(),
                                    categoryTitle = selected.title
                                )
                            ) {
                                popUpTo(Screens.ChatCategory.route) {
                                    inclusive = true
                                }
                            }
                        },
                        enabled = uiState.selectedCategory != null &&
                                !uiState.isLoading &&
                                !uiState.isRefreshing,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.primary,
                            contentColor = colors.onPrimary,
                            disabledContainerColor = colors.onSurface.copy(alpha = 0.12f),
                            disabledContentColor = colors.onSurface.copy(alpha = 0.38f)
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.btn_new_ticket),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatCategoryTopBar(
    customerName: String,
    onBackClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(colors.primary)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.avatar),
                    contentDescription = stringResource(R.string.desc_customer_avatar),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, colors.onPrimary, CircleShape)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = customerName,
                    color = colors.onPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 12.dp)
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = stringResource(R.string.desc_close_screen),
                tint = colors.onPrimary,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = onBackClick)
            )
        }
    }
}

@Composable
private fun CategoryDropdownSection(
    modifier: Modifier = Modifier,
    isDropdownExpanded: Boolean,
    onDropdownExpandedChange: (Boolean) -> Unit,
    uiState: ChatCategoryUiState,
    onCategorySelected: (com.smartbank.smarthamrah.features.customers.chat.domain.model.ChatCategory) -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = stringResource(R.string.label_select_category),
            fontSize = 13.sp,
            color = colors.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .background(colors.surface, RoundedCornerShape(8.dp))
                .border(1.dp, colors.outlineVariant, RoundedCornerShape(8.dp))
                .clickable(
                    enabled = !uiState.isLoading &&
                            !uiState.isRefreshing &&
                            uiState.categories.isNotEmpty()
                ) {
                    onDropdownExpandedChange(!isDropdownExpanded)
                }
                .padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = uiState.selectedCategory?.title
                    ?: stringResource(R.string.placeholder_select_category),
                color = if (uiState.selectedCategory == null) {
                    colors.onSurfaceVariant.copy(alpha = 0.5f)
                } else {
                    colors.onSurface
                },
                fontSize = 14.sp
            )

            if (uiState.isLoading || uiState.isRefreshing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = colors.primary
                )
            } else {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = colors.onSurfaceVariant
                )
            }
        }

        if (uiState.errorMessage != null) {
            Text(
                text = uiState.errorMessage,
                color = colors.error,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        if (isDropdownExpanded) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colors.surface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 3.dp
                )
            ) {
                Column {
                    uiState.categories.forEachIndexed { index, category ->
                        Text(
                            text = category.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onCategorySelected(category)
                                }
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            fontSize = 14.sp,
                            color = colors.onSurface
                        )

                        if (index < uiState.categories.lastIndex) {
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = colors.outlineVariant.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }
        }
    }
}