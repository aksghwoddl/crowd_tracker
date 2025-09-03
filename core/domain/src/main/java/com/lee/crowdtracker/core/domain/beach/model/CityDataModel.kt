package com.lee.crowdtracker.core.domain.beach.model

data class CityDataModel(
    val name: String,
    val congestionLevel: CongestionLevel,
    val congestionMessage: String,
)