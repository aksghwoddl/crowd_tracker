package com.lee.crowdtracker.search.model

import androidx.compose.runtime.Stable
import com.lee.crowdtracker.core.domain.beach.model.AreaModel

@Stable
data class Area(
    val no: Int,
    val name: String,
    val category: String,
)

fun AreaModel.toArea() = Area(
    no = no,
    name = name,
    category = category
)