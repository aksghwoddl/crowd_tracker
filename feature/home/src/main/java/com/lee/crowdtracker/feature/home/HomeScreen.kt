package com.lee.crowdtracker.feature.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lee.crowdtracker.feature.home.component.CongestionLegend
import com.lee.crowdtracker.feature.home.component.SelectedMarkerCard
import com.lee.crowdtracker.libray.design.theme.CDTheme
import com.lee.crowdtracker.libray.navermap.componenrt.NaverMapView
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons

private const val TAG = "HomeScreen"

@Composable
fun HomeRoute(
    onShowSnackBar: suspend (String, String?) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val state by homeViewModel.uiState.collectAsStateWithLifecycle()
    val selectedMarkerId by homeViewModel.selectedMarkerId.collectAsStateWithLifecycle()

    HomeScreen(
        state = state,
        selectedMarkerId = selectedMarkerId,
        onRetry = homeViewModel::onRetry,
        onMarkerClick = homeViewModel::onMarkerClick,
        onSelectedMarkerCardDismiss = homeViewModel::onSelectedMarkerCardDismiss
    )
}

@Composable
internal fun HomeScreen(
    state: HomeUiState,
    selectedMarkerId: String,
    onRetry: () -> Unit,
    onMarkerClick: (String) -> Unit,
    onSelectedMarkerCardDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var naverMap by remember { mutableStateOf<NaverMap?>(null) }

    val markerOverlays by produceState(
        key1 = state is HomeUiState.Success,
        key2 = naverMap,
        initialValue = emptyMap()
    ) {
        when (state) {
            is HomeUiState.Success -> {
                state.crowdMarkerData
                    .filter { it.latLng != LatLngBounds.INVALID }
                    .forEach { markerData ->
                        value = buildMap {
                            put(
                                key = markerData.id,
                                value = Marker().apply {
                                    setOnClickListener {
                                        onMarkerClick(markerData.id)
                                        naverMap?.moveCamera(CameraUpdate.scrollTo(position))
                                        true
                                    }
                                    position = markerData.latLng
                                    icon = MarkerIcons.BLACK
                                    iconTintColor = markerData.level.color.toInt()
                                    captionText = markerData.name
                                    captionColor = markerData.level.color.toInt()
                                    captionHaloColor = android.graphics.Color.WHITE
                                    zIndex = if (markerData.id == selectedMarkerId) 1 else 0
                                    map = naverMap
                                }
                            )
                        }
                    }
                naverMap?.locationOverlay?.isVisible = true
            }

            else -> {
                value = emptyMap()
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        NaverMapView(
            modifier = Modifier.fillMaxSize(),
            onMapReady = { naverMap = it },
        )

        when (state) {
            is HomeUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is HomeUiState.Success -> {
                CongestionLegend(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp),
                )

                if (selectedMarkerId.isNotEmpty()) {
                    state.crowdMarkerData.find { it.id == selectedMarkerId }?.let {
                        SelectedMarkerCard(
                            name = it.name,
                            category = it.category,
                            description = it.description,
                            congestionColor = Color(it.level.color),
                            onDismiss = onSelectedMarkerCardDismiss,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(16.dp)
                        )
                    }
                }
            }

            is HomeUiState.Error -> {
                ErrorContent(
                    message = state.message,
                    onRetry = onRetry,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            markerOverlays.values.forEach { it.map = null }
            naverMap = null
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
@Preview(showSystemUi = true)
private fun HomeScreenLoadingPreview() {
    CDTheme {
        HomeScreen(
            state = HomeUiState.Loading,
            selectedMarkerId = "",
            onRetry = {},
            onMarkerClick = {},
            onSelectedMarkerCardDismiss = {}
        )
    }
}

@Composable
@Preview(showSystemUi = true)
private fun HomeScreenErrorPreview() {
    CDTheme {
        HomeScreen(
            state = HomeUiState.Error(message = "혼잡도 정보를 불러오지 못했습니다."),
            selectedMarkerId = "",
            onRetry = {},
            onMarkerClick = {},
            onSelectedMarkerCardDismiss = {}
        )
    }
}
