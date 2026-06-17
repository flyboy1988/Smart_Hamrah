import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SmoothProgressBar(
    targetProgress: Float,
    animated: Boolean = false
) {
    val progress = targetProgress.coerceIn(0.01f, 1f)

    val farsiPercent = (progress * 100).toInt().toString()
        .replace('0', '۰').replace('1', '۱').replace('2', '۲')
        .replace('3', '۳').replace('4', '۴').replace('5', '۵')
        .replace('6', '۶').replace('7', '۷').replace('8', '۸')
        .replace('9', '۹')

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(14.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF00BEC6))
            )

            val horizontalBias = (progress * 2) - 1

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = BiasAlignment(horizontalBias, 0f)
            ) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFB2EBF2)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$farsiPercent٪",
                        color = Color(0xFF00838F),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}