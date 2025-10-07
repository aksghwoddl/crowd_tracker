package com.lee.crowdtracker.libray.navermap

import com.naver.maps.geometry.LatLng

interface NaverMapSdkController {
    fun init()
    suspend fun getLatLngByName(
        name: String,
        onCancellation: (Throwable) -> Unit,
    ): LatLng
}