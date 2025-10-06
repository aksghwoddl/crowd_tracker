package com.lee.crowdtracker.libray.navermap.componenrt

import android.location.Geocoder
import android.util.Log
import android.view.ContextThemeWrapper
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.lee.crowdtracker.libray.design.R
import com.lee.crowdtracker.libray.navermap.LocalFusedLocationSource
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

private const val TAG = "NaverMapView"

@Composable
fun rememberMapViewWithLifeCycle(): MapView {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val mapView = remember {
        MapView(
            ContextThemeWrapper(
                context,
                R.style.Theme_CrowdTracker_AppCompat,
            )
        )
    }

    DisposableEffect(lifecycle) {
        val observer = object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) = mapView.onCreate(null)
            override fun onStart(owner: LifecycleOwner) = mapView.onStart()
            override fun onResume(owner: LifecycleOwner) = mapView.onResume()
            override fun onPause(owner: LifecycleOwner) = mapView.onPause()
            override fun onStop(owner: LifecycleOwner) = mapView.onStop()
            override fun onDestroy(owner: LifecycleOwner) = mapView.onDestroy()
        }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }

    return mapView
}

@Composable
fun NaverMapView(
    modifier: Modifier = Modifier,
    markers: List<CrowdMarkerEntry> = emptyList(),
    selectedMarkerId: String? = null,
    onMarkerClick: (CrowdMarkerEntry) -> Unit = {},
) {
    val mapView = rememberMapViewWithLifeCycle()
    val locationSource = LocalFusedLocationSource.current
    val context = LocalContext.current
    val geocoder = remember(context) { Geocoder(context, Locale.KOREA) }
    var naverMap by remember { mutableStateOf<NaverMap?>(null) }
    val markerOverlays = remember { mutableStateMapOf<String, Marker>() }
    val coordinateCache = remember { mutableStateMapOf<String, LatLng>() }
    var hasInitializedCamera by remember { mutableStateOf(false) }

    DisposableEffect(mapView) {
        mapView.getMapAsync { map ->
            map.minZoom = 5.5
            map.uiSettings.isLocationButtonEnabled = true
            map.locationSource = locationSource
            map.locationTrackingMode = LocationTrackingMode.Follow
            map.locationOverlay.isVisible = true
            naverMap = map
        }
        onDispose {
            markerOverlays.values.forEach { it.map = null }
            markerOverlays.clear()
            coordinateCache.clear()
            hasInitializedCamera = false
            naverMap = null
        }
    }

    LaunchedEffect(naverMap, markers) {
        val map = naverMap ?: return@LaunchedEffect

        if (!Geocoder.isPresent()) {
            Log.w(TAG, "이 기기에서 지오코더를 사용할 수 없어 마커를 표시하지 못했습니다.")
            return@LaunchedEffect
        }

        if (markers.isEmpty()) {
            markerOverlays.values.forEach { it.map = null }
            markerOverlays.clear()
            hasInitializedCamera = false
            return@LaunchedEffect
        }

        val missingEntries = markers.filter { entry -> coordinateCache[entry.id] == null }
        if (missingEntries.isNotEmpty()) {
            val resolved = withContext(Dispatchers.IO) {
                missingEntries.mapNotNull { entry ->
                    val keyword = entry.searchKeyword.ifBlank { entry.title }
                    runCatching {
                        geocoder.getFromLocationName(keyword, 1)
                    }.onFailure { error ->
                        Log.e(TAG, "지오코딩 실패 ($keyword)", error)
                    }.getOrNull()
                        ?.firstOrNull()
                        ?.let { address ->
                            entry.id to LatLng(address.latitude, address.longitude)
                        }
                }
            }
            resolved.forEach { (id, latLng) ->
                coordinateCache[id] = latLng
            }
        }

        val activeIds = mutableSetOf<String>()
        val boundsBuilder = LatLngBounds.Builder()
        markers.forEach { entry ->
            val latLng = coordinateCache[entry.id] ?: return@forEach
            activeIds += entry.id

            val marker = markerOverlays.getOrPut(entry.id) { Marker() }
            marker.position = latLng
            marker.icon = MarkerIcons.BLACK
            marker.iconTintColor = entry.tintColor
            marker.captionText = entry.title
            marker.captionColor = entry.tintColor
            marker.captionHaloColor = android.graphics.Color.WHITE
            marker.map = map
            marker.setOnClickListener {
                onMarkerClick(entry)
                map.moveCamera(CameraUpdate.scrollTo(marker.position))
                true
            }
            marker.zIndex = if (entry.id == selectedMarkerId) 1 else 0
            boundsBuilder.include(latLng)
        }

        val staleIds = markerOverlays.keys - activeIds
        staleIds.forEach { id ->
            markerOverlays.remove(id)?.map = null
            coordinateCache.remove(id)
        }

        if (!hasInitializedCamera && activeIds.isNotEmpty()) {
            runCatching {
                val bounds = boundsBuilder.build()
                map.moveCamera(CameraUpdate.fitBounds(bounds, 80))
                hasInitializedCamera = true
            }.onFailure { error ->
                Log.e(TAG, "카메라 초기화 실패", error)
            }
        }
    }

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { mapView }
    )
}
