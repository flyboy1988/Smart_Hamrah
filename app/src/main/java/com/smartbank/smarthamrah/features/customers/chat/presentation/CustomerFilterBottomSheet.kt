package com.smartbank.smarthamrah.features.customers.chat.presentation

import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogWindowProvider

data class FilterOption(
    val id: String,
    val label: String
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerFilterBottomSheet(
    onDismiss: () -> Unit,
    onApplyFilters: (List<String>) -> Unit
) {
    val colors = MaterialTheme.colorScheme

    // Checklist options string data source definitions
    val filterOptions = remember {
        listOf(
            FilterOption(id = "unread", label = "بررسی تراکنش"), // Unread messages
            FilterOption(id = "sms_issue", label = "درخواست وام"), // SMS notification issues
            FilterOption(id = "card_blocked", label = "کارت\u200Cهای بانکی"), // Blocked cards/accounts
            FilterOption(id = "loan_inquiry", label = "مسدودی کارت و حساب"), // Loan inquiries
            FilterOption(id = "open_tickets", label = "قطع پیامک واریزی") // Currently active/open tickets
        )
    }

    // State tracking dictionary map for each row choice option item key
    val selectedStates = remember {
        mutableStateMapOf<String, Boolean>().apply {
            filterOptions.forEach { this[it.label] = false }
        }
    }

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = Color(0xFFF4F4F4), // Light surface overlay behind the white inner card container box
            dragHandle = null,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            val windowProvider = LocalView.current.parent as? DialogWindowProvider
            windowProvider?.window?.let { dialogWindow ->
                dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                // HEADER BLOCK (Matching Ticket Styling Structure Layout rules)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.primary)
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "فیلتر ها", // "Filters"
                        color = colors.onPrimary,
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = null,
                            tint = colors.onPrimary
                        )
                    }
                }

                // FILTER BODY SECTION
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {

                    Text(
                        text = "مرتب سازی بر اساس",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
                    )

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = colors.surface,
                        tonalElevation = 1.dp
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                            filterOptions.forEach { option ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = option.label, // Displays the clean Persian text label
                                        fontSize = 14.sp,
                                        color = colors.onSurface
                                    )
                                    Checkbox(
                                        // Looks up selection state via unique ID key string token
                                        checked = selectedStates[option.id] ?: false,
                                        onCheckedChange = { isChecked ->
                                            selectedStates[option.id] = isChecked
                                        },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = colors.primary,
                                            uncheckedColor = colors.outlineVariant
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // ... (Inside your Button action logic block below)
                    Button(
                        onClick = {
                            // Collects only the active IDs to pass back to your ViewModel query pipeline
                            val activeFilterIds = selectedStates.filter { it.value }.keys.toList()
                            onApplyFilters(activeFilterIds)
                        }
                    ) {
                        Text(text = "اعمال فیلتر")
                    }
                }
}}}}
