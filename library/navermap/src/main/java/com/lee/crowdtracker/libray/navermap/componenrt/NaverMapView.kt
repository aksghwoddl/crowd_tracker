package com.lee.crowdtracker.libray.navermap.componenrt

import android.view.ContextThemeWrapper
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.lee.crowdtracker.libray.design.R
import com.lee.crowdtracker.libray.navermap.LocalFusedLocationSource
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap

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
    onMapReady: (NaverMap) -> Unit,
) {
    val mapView = rememberMapViewWithLifeCycle()
    val locationSource = LocalFusedLocationSource.current

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = {
            mapView.getMapAsync { map ->
                map.minZoom = 5.5
                map.uiSettings.isLocationButtonEnabled = true
                map.locationSource = locationSource
                map.locationTrackingMode = LocationTrackingMode.Follow
                map.locationOverlay.isVisible = true
                onMapReady(map)
            }
            mapView
        }
    )
}
