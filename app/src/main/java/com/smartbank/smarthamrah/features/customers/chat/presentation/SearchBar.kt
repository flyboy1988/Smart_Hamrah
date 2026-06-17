package com.smartbank.smarthamrah.features.customers.chat.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartbank.smarthamrah.R

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onMenuClick: () -> Unit, // Added callback for navigation
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .border(
                border = BorderStroke(1.dp, colors.primary),
                shape = RoundedCornerShape(12.dp)
            )
            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(12.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            placeholder = {
                Text(
                    text = "جستجو",
                    color = colors.primary,
                    fontSize = 15.sp,
                    style = MaterialTheme.typography.bodyMedium.copy(textDirection = TextDirection.Rtl)
                )
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .background(colors.primary)
        )

        // The Menu Button on the left side
        IconButton(
            onClick = onMenuClick, // Trigger the navigation callback here
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .size(28.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.filtermenu),
                contentDescription = null,
                tint = colors.primary
            )
        }
    }
}