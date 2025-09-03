package com.lee.crowdtracker.search.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lee.crowdtracker.core.domain.beach.model.CongestionLevel
import com.lee.crowdtracker.libray.design.theme.CDTheme
import com.lee.crowdtracker.search.CityDataUiState

@Composable
fun CityContentsDialog(
    cityDataUiState: CityDataUiState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (cityDataUiState) {
        is CityDataUiState.Success -> {
            ContentsDialog(
                name = cityDataUiState.name,
                level = cityDataUiState.level,
                message = cityDataUiState.message,
                onDismiss = onDismiss
            )
        }

        CityDataUiState.Loading -> {
            LoadingDialog(
                modifier = modifier,
                onDismiss = onDismiss,
            )
        }
    }
}

@Composable
private fun ContentsDialog(
    name: String,
    level: CongestionLevel,
    message: String,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
) {
    val congestionColor = levelColors(level)
    AlertDialog(
        modifier = modifier.fillMaxWidth(),
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
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        Modifier
                            .size(10.dp)
                            .drawBehind(
                                onDraw = {
                                    drawCircle(
                                        color = congestionColor
                                    )
                                }
                            )
                    )
                    Text(
                        text = level.label,
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
private fun LoadingDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = {
            Text(
                text = "불러오는 중...",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Box {
                Column(
                    modifier = modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator()
                    Text(
                        text = "도시 데이터를 가져오는 중입니다.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
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
            cityDataUiState = CityDataUiState.Success(
                name = "홍대입구역",
                level = CongestionLevel.HIGH,
                message = "붐비고 있습니다!"
            ),
            onDismiss = {}
        )
    }
}

@Preview
@Composable
private fun CityContentsDialogLaadingPreview() {
    CDTheme {
        CityContentsDialog(
            cityDataUiState = CityDataUiState.Loading,
            onDismiss = {}
        )
    }
}