package com.smartbank.smarthamrah.features.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.smartbank.smarthamrah.R

@Composable
fun LogoutCard(
    isLoading: Boolean = false,
    onLogoutClick: () -> Unit
) {

    var showDialog by remember {
        mutableStateOf(false)
    }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            showDialog = true
        },
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(id = R.drawable.exit),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(R.string.logout),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

    if (showDialog) {

        AlertDialog(
            onDismissRequest = {
                if (!isLoading) {
                    showDialog = false
                }
            },

            icon = {
                Icon(
                    painter = painterResource(R.drawable.exit),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(28.dp)
                )
            },

            title = {
                Text(
                    text = "خروج از حساب کاربری",
                    style = MaterialTheme.typography.titleMedium
                )
            },

            text = {
                Text(
                    text = "آیا از خروج از حساب کاربری اطمینان دارید؟ برای استفاده مجدد باید دوباره وارد شوید.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },

            confirmButton = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    OutlinedButton(
                        enabled = !isLoading,
                        onClick = {
                            showDialog = false
                        }
                    ) {
                        Text("انصراف")
                    }

                    Button(
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ),
                        onClick = {
                            showDialog = false
                            onLogoutClick()
                        }
                    ) {

                        if (isLoading) {

                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = MaterialTheme.colorScheme.onError,
                                strokeWidth = 2.dp
                            )

                        } else {

                            Text("خروج")
                        }
                    }
                }
            },

            shape = RoundedCornerShape(20.dp),

            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}