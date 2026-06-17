package com.smartbank.smarthamrah.features.ticket

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.smartbank.smarthamrah.R
import java.io.ByteArrayOutputStream

@Composable
fun NewTicketScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    onSubmit: (group: String, title: String, description: String, attachment: ByteArray?) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val context = LocalContext.current

    // Create the scroll state for the form container
    val scrollState = rememberScrollState()

    var selectedGroup by remember { mutableStateOf("") }
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var selectedFileName by remember { mutableStateOf<String?>(null) }
    var attachmentBytes by remember { mutableStateOf<ByteArray?>(null) }

    val groups = listOf(
        "بانکداری دیجیتال و اپلیکیشن",
        "تسهیلات و وام",
        "کارت‌های بانکی",
        "حساب‌ها",
        "انتقادات و پیشنهادات"
    )
    var expanded by remember { mutableStateOf(false) }

    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            val mimeType = context.contentResolver.getType(it)
            if (mimeType == "application/pdf" || mimeType?.startsWith("image/") == true) {
                context.contentResolver.openInputStream(it)?.use { inputStream ->
                    val buffer = ByteArrayOutputStream()
                    inputStream.copyTo(buffer)
                    attachmentBytes = buffer.toByteArray()
                }
                selectedFileName = it.lastPathSegment ?: "سند آپلود شده"
            } else {
                android.widget.Toast.makeText(context, "فقط PDF یا عکس مجاز است", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Form container made dynamically scrollable
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState), // <-- Adds vertical scrolling capability
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // گروه (Inline Dropdown Expansion Approach)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "انتخاب دسته‌بندی",
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
                        .clickable { expanded = !expanded }
                        .padding(horizontal = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedGroup.ifEmpty { "انتخاب گروه" },
                        color = if (selectedGroup.isEmpty()) colors.onSurfaceVariant.copy(alpha = 0.5f) else colors.onSurface,
                        fontSize = 14.sp
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = colors.onSurfaceVariant
                    )
                }

                // Inline expansion structure
                if (expanded) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column {
                            groups.forEachIndexed { index, group ->
                                Text(
                                    text = group,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedGroup = group
                                            expanded = false
                                        }
                                        .padding(horizontal = 16.dp, vertical = 14.dp),
                                    fontSize = 14.sp,
                                    color = colors.onSurface
                                )
                                if (index < groups.lastIndex) {
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

            // عنوان
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("عنوان", fontSize = 12.sp, color = colors.onSurfaceVariant, fontWeight = FontWeight.Medium)
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("متن", fontSize = 13.sp, color = colors.onSurfaceVariant.copy(alpha = 0.5f)) },
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colors.surface,
                        unfocusedContainerColor = colors.surface,
                        focusedBorderColor = colors.primary,
                        unfocusedBorderColor = colors.outlineVariant
                    ),
                    singleLine = true
                )
            }

            // شرح موضوع
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("شرح موضوع...", fontSize = 12.sp, color = colors.onSurfaceVariant, fontWeight = FontWeight.Medium)
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    placeholder = { Text("شرح موضوع...", fontSize = 13.sp, color = colors.onSurfaceVariant.copy(alpha = 0.5f)) },
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colors.surface,
                        unfocusedContainerColor = colors.surface,
                        focusedBorderColor = colors.primary,
                        unfocusedBorderColor = colors.outlineVariant
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
            }

            // Upload file button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Surface(
                    modifier = Modifier
                        .clickable { filePicker.launch(arrayOf("application/pdf", "image/*")) },
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFD7E3F4),
                    border = BorderStroke(1.dp, Color(0xFF1A56A6))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.uploadf),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),

                        )
                        Text(
                            text = selectedFileName ?: "آپلود فایل",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1A56A6)
                        )

                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // This stays locked outside the scroll view at the very bottom
        Button(
            onClick = {
                if (selectedGroup.isNotBlank() && title.text.isNotBlank() && description.text.isNotBlank()) {
                    onSubmit(selectedGroup, title.text, description.text, attachmentBytes)
                }
            },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primary,
                contentColor = colors.onPrimary,
                disabledContainerColor = colors.onSurface.copy(alpha = 0.12f),
                disabledContentColor = colors.onSurface.copy(alpha = 0.38f)
            ),
            enabled = selectedGroup.isNotBlank() && title.text.isNotBlank() && description.text.isNotBlank()
        ) {

            Text("ثبت تیکت", fontSize = 16.sp, fontWeight = FontWeight.Normal)
        }
    }
}