package com.smartbank.smarthamrah.features.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.smartbank.smarthamrah.R
import com.smartbank.smarthamrah.core.utils.PreferenceManager

@Composable
fun UserProfileImage(
    imageUrl: String?,
    onClick: () -> Unit = {}
) {

    val imageModifier = Modifier
        .size(36.dp)
        .clip(CircleShape)
        .border(2.dp, Color(0xFF8DC9FF), CircleShape)
        .clickable { onClick() }

    if (imageUrl.isNullOrBlank()) {
        DefaultAvatar(modifier = imageModifier)
    } else {
        AsyncImage(
            model = imageUrl,
            contentDescription = "User Profile",
            modifier = imageModifier,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.user1),
            error = painterResource(id = R.drawable.user1)
        )
    }
}

@Composable
fun DefaultAvatar(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.user1),
        contentDescription = "Default Avatar",
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}