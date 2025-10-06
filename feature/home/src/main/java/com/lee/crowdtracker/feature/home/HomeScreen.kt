package com.lee.crowdtracker.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lee.crowdtracker.core.domain.beach.model.CongestionLevel
import com.lee.crowdtracker.libray.design.theme.CDTheme
import com.lee.crowdtracker.libray.navermap.componenrt.CrowdMarkerEntry
import com.lee.crowdtracker.libray.navermap.componenrt.NaverMapView
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close

@Composable
fun HomeRoute(
    onShowSnackBar: suspend (String, String?) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    val errorMessage = (uiState as? HomeUiState.Error)?.message
    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            onShowSnackBar(errorMessage, null)
        }
    }

    HomeScreen(
        uiState = uiState,
        onRetry = homeViewModel::refresh,
    )
}

@Composable
internal fun HomeScreen(
    uiState: HomeUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val successState = uiState as? HomeUiState.Success
    val markerEntries = successState?.markers?.map { marker ->
        CrowdMarkerEntry(
            id = marker.id.toString(),
            title = marker.name,
            description = marker.congestionMessage,
            searchKeyword = "${marker.name} 서울",
            tintColor = congestionColor(marker.congestionLevel).toArgb(),
        )
    } ?: emptyList()

    var selectedMarkerId by rememberSaveable { mutableStateOf<String?>(null) }
    val markerLookup = remember(successState) {
        successState?.markers?.associateBy { it.id.toString() } ?: emptyMap()
    }

    LaunchedEffect(markerEntries) {
        if (selectedMarkerId != null && markerLookup[selectedMarkerId] == null) {
            selectedMarkerId = null
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        NaverMapView(
            modifier = Modifier.fillMaxSize(),
            markers = markerEntries,
            selectedMarkerId = selectedMarkerId,
            onMarkerClick = { entry ->
                selectedMarkerId = entry.id
            }
        )

        when (uiState) {
            HomeUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is HomeUiState.Error -> {
                ErrorContent(
                    message = uiState.message,
                    onRetry = onRetry,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is HomeUiState.Success -> {
                CongestionLegend(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                )
                markerLookup[selectedMarkerId]?.let { selected ->
                    SelectedMarkerCard(
                        marker = selected,
                        onDismiss = { selectedMarkerId = null },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SelectedMarkerCard(
    marker: AreaCongestionUiModel,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Spacer(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(RoundedCornerShape(percent = 50))
                            .background(congestionColor(marker.congestionLevel))
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = marker.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = marker.category,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "닫기"
                    )
                }
            }
            Text(
                text = marker.congestionMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        tonalElevation = 6.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Button(onClick = onRetry) {
                Text(text = "다시 시도")
            }
        }
    }
}

@Composable
private fun CongestionLegend(
    modifier: Modifier = Modifier,
    indicatorSize: Dp = 10.dp,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "혼잡도 안내",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            CongestionLevel.entries
                .filter { it != CongestionLevel.UNKNOWN }
                .forEach { level ->
                    LegendRow(
                        label = level.label,
                        color = congestionColor(level),
                        indicatorSize = indicatorSize
                    )
                }
        }
    }
}

@Composable
private fun LegendRow(
    label: String,
    color: Color,
    indicatorSize: Dp,
) {
    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .size(indicatorSize)
                .clip(RoundedCornerShape(percent = 50))
                .background(color)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

private fun congestionColor(level: CongestionLevel): Color = when (level) {
    CongestionLevel.LOW -> Color(0xFF2E7D32)
    CongestionLevel.MODERATE -> Color(0xFF1976D2)
    CongestionLevel.HIGH -> Color(0xFFF57C00)
    CongestionLevel.SEVERE -> Color(0xFFC62828)
    CongestionLevel.UNKNOWN -> Color(0xFF9E9E9E)
}

@Composable
@Preview(showSystemUi = true)
private fun HomeScreenPreview() {
    CDTheme {
        HomeScreen(
            uiState = HomeUiState.Success(
                markers = listOf(
                    AreaCongestionUiModel(
                        id = 1,
                        name = "강남역",
                        category = "인구밀집지역",
                        congestionLevel = CongestionLevel.HIGH,
                        congestionMessage = "약간 붐비는 편이에요.",
                    ),
                    AreaCongestionUiModel(
                        id = 2,
                        name = "잠실",
                        category = "발달상권",
                        congestionLevel = CongestionLevel.LOW,
                        congestionMessage = "여유로운 상태입니다.",
                    )
                )
            ),
            onRetry = {}
        )
    }
}

@Composable
@Preview(showSystemUi = true)
private fun HomeScreenLoadingPreview() {
    CDTheme {
        HomeScreen(
            uiState = HomeUiState.Loading,
            onRetry = {}
        )
    }
}

@Composable
@Preview(showSystemUi = true)
private fun HomeScreenErrorPreview() {
    CDTheme {
        HomeScreen(
            uiState = HomeUiState.Error(message = "혼잡도 정보를 불러오지 못했습니다."),
            onRetry = {}
        )
    }
}
