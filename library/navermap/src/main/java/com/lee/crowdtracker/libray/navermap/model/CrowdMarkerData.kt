package com.lee.crowdtracker.libray.navermap.model

import androidx.compose.runtime.Immutable
import com.lee.crowdtracker.core.domain.beach.model.CongestionLevel
import com.naver.maps.geometry.LatLng

@Immutable
data class CrowdMarkerData(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val searchKeyword: String = name,
    val level: CongestionLevel,
    val latLng: LatLng
)