package com.lee.crowdtracker.libray.navermap

import androidx.compose.runtime.staticCompositionLocalOf
import com.naver.maps.map.util.FusedLocationSource

val LocalFusedLocationSource = staticCompositionLocalOf<FusedLocationSource> {
    error("not initialized FusedLocationSource")
}

