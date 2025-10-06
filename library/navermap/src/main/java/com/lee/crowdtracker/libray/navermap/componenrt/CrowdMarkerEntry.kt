package com.lee.crowdtracker.libray.navermap.componenrt

import androidx.annotation.ColorInt
import androidx.compose.runtime.Immutable

@Immutable
data class CrowdMarkerEntry(
    val id: String,
    val title: String,
    val description: String,
    val searchKeyword: String = title,
    @ColorInt val tintColor: Int,
)
