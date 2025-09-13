package com.lee.crowdtracker.libray.navermap.componenrt

import android.R.style
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
import com.naver.maps.map.MapView

@Composable
fun rememberMapViewWithLifeCycle(): MapView {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val mapView = remember {
        MapView(
            ContextThemeWrapper(
                context,
                style.Theme
            )
        )
    }

    DisposableEffect(lifecycle) {
        val observer = object : DefaultLifecycleObserver {
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
) {
    val mapView = rememberMapViewWithLifeCycle()

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { mapView },
        update = {
            it.getMapAsync { naverMap ->
                naverMap.minZoom = 5.5
                naverMap.uiSettings.isLocationButtonEnabled = true
            }
        }
    )
}