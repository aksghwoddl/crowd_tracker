package com.lee.crowdtracker.core.domain.beach.model

data class CrowdDataModel(
    val id: Int = 0,
    val name: String = "",
    val category: String = "",
    val congestionLevel: CongestionLevel = CongestionLevel.UNKNOWN,
    val congestionMessage: String = "",
)