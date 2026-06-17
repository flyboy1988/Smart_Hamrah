package com.smartbank.smarthamrah.features.customers

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.smartbank.smarthamrah.R
import com.smartbank.smarthamrah.core.navigation.ScreenWrapper
import com.smartbank.smarthamrah.core.navigation.Screens
import com.smartbank.smarthamrah.features.customers.domain.model.BankerCustomer

@Composable
fun CustomersScreen(
    navController: NavController,
    viewModel: CustomersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val customersList = uiState.customers
    val dynamicTitle = "مشتریان (${customersList.size})"
    LaunchedEffect(Unit) {
        viewModel.navigation.collect { navigation ->
            when (navigation) {
                is CustomersNavigation.Chat -> {
                    navController.navigate(
                        Screens.CustomerChat.createRoute(
                            customerName = navigation.customerName,
                            categoryId = navigation.categoryId,
                            categoryTitle = navigation.categoryTitle,
                            customerId = navigation.customerId
                        )
                    )
                }

                is CustomersNavigation.ChatCategory -> {
                    navController.navigate(
                        Screens.ChatCategory.createRoute(
                            customerName = navigation.customerName,
                            customerId = navigation.customerId
                        )
                    )
                }
            }
        }
    }
    ScreenWrapper(
        title = dynamicTitle,
        navController = navController,
        showBackButton = true
    ) { modifier ->

        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
            ) {
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = viewModel::onSearchQueryChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    placeholder = {
                        Text(
                            text = "جستجوی مشتری",
                            fontSize = 13.sp
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            viewModel.searchCustomers()
                        }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color(0xFFB6B7BD),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    when {
                        uiState.isLoading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        uiState.errorMessage != null -> {
                            Column(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = uiState.errorMessage.orEmpty(),
                                    color = MaterialTheme.colorScheme.error,
                                    fontSize = 14.sp
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Button(
                                    onClick = {
                                        viewModel.loadCustomers(
                                            customerName = uiState.searchQuery.takeIf {
                                                it.isNotBlank()
                                            }
                                        )
                                    },
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("تلاش مجدد")
                                }
                            }
                        }

                        customersList.isEmpty() -> {
                            Text(
                                text = "مشتری‌ای برای نمایش وجود ندارد",
                                modifier = Modifier.align(Alignment.Center),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp
                            )
                        }

                        else -> {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(
                                    start = 16.dp,
                                    end = 16.dp,
                                    bottom = 16.dp
                                ),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(
                                    items = customersList,
                                    key = { it.customerId }
                                ) { customer ->
                                    CustomerListItem(
                                        item = customer,
                                        navController = navController,
                                        onMessageClick = viewModel::onMessageClick
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomerListItem(
    item: BankerCustomer,
    navController: NavController,
    onMessageClick: (BankerCustomer) -> Unit
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                expanded = !expanded
            },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "نام و نام خانوادگی",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )

                    Text(
                        text = item.fullName.ifBlank { "مشتری" },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF212121)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "شماره مشتری",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.customerNumber.ifBlank { "-" },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF212121)
                    )
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(14.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = Color(0xFFB6B7BD),
                            shape = RoundedCornerShape(10.dp)
                        )
                ) {
                    CustomerDetailRow(
                        title = "نوع حساب",
                        value = item.accountTypeTitle.ifBlank { "-" }
                    )

                    CustomerDetailRow(
                        title = "تاریخ افتتاح حساب",
                        value = item.accountOpeningDate.ifBlank { "-" }
                    )

                    CustomerDetailRow(
                        title = "شماره کارت",
                        value = "-"
                    )

                    CustomerDetailRow(
                        title = "شماره شبا",
                        value = "-"
                    )

                    CustomerDetailRow(
                        title = "رتبه مشتری",
                        value = "-"
                    )

                    CustomerDetailRow(
                        title = "تعداد پیام",
                        value = "${item.messageCount} پیام"
                    )

                    CustomerDetailRow(
                        title = "تعداد تماس",
                        value = "${item.callCount} تماس",
                        showDivider = false
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    onClick = {
                        onMessageClick(item)
                    },
                    modifier = Modifier.size(
                        height = 34.dp,
                        width = 48.dp
                    ),
                    shape = RoundedCornerShape(7.dp),
                    color = Color(0xFF0052B4)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(id = R.drawable.mail),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    onClick = {
                        if (item.mobileNumber.isNotBlank()) {
                            val intent = Intent(
                                Intent.ACTION_DIAL,
                                Uri.parse("tel:${item.mobileNumber}")
                            )
                            context.startActivity(intent)
                        }
                    },
                    modifier = Modifier.size(
                        height = 34.dp,
                        width = 48.dp
                    ),
                    shape = RoundedCornerShape(7.dp),
                    color = Color(0xFF00B4D8)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(id = R.drawable.call),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomerDetailRow(
    title: String,
    value: String,
    showDivider: Boolean = true
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 9.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value,
                fontSize = 12.sp,
                color = Color(0xFF212121),
                fontWeight = FontWeight.Medium
            )

            Text(
                text = title,
                fontSize = 11.sp,
                color = Color.Gray
            )
        }

        if (showDivider) {
            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFFE0E0E0),
                modifier = Modifier.padding(horizontal = 14.dp)
            )
        }
    }
}