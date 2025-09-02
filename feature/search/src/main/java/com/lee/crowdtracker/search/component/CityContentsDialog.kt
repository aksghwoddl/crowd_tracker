package com.lee.crowdtracker.search.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lee.crowdtracker.libray.design.theme.CDTheme

enum class CongestionLevel {
    LOW,
    MODERATE,
    HIGH,
    SEVERE,
    UNKNOWN,
    ;

    companion object {
        fun fromLabel(label: String): CongestionLevel {
            return when (label) {
                "여유" -> LOW
                "보통" -> MODERATE
                "약간 붐빔" -> HIGH
                "붐빔" -> SEVERE
                else -> UNKNOWN
            }
        }
    }
}

@Composable
fun CityContentsDialog(
    name: String,
    level: String,
    message: String,
    onDismiss: () -> Unit
) {
    val congestionColor = levelColors(CongestionLevel.fromLabel(level))

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("확인")
            }
        },
        title = {
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(congestionColor)
                    )
                    Text(
                        text = level,
                        style = MaterialTheme.typography.labelLarge,
                        color = congestionColor
                    )
                }
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    )
}

@Composable
private fun levelColors(level: CongestionLevel): Color = when (level) {
    CongestionLevel.LOW -> Color(0xFF2E7D32)
    CongestionLevel.MODERATE -> Color(0xFF1976D2)
    CongestionLevel.HIGH -> Color(0xFFF57C00)
    CongestionLevel.SEVERE -> Color(0xFFC62828)
    CongestionLevel.UNKNOWN -> Color(0xFF9E9E9E)
}

@Preview
@Composable
private fun CityContentsDialogPreview() {
    CDTheme {
        CityContentsDialog(
            name = "홍제폭포",
            level = "보통",
            message = "보통입니다.",
        ) { }
    }
}