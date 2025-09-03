package com.lee.crowdtracker.core.domain.beach.model

enum class CongestionLevel(val label: String) {
    LOW(label = "여유"),
    MODERATE(label = "보통"),
    HIGH(label = "약간 붐빔"),
    SEVERE("붐빔"),
    UNKNOWN(label = ""),
    ;

    companion object {
        fun fromLabel(label: String): CongestionLevel {
            return when (label) {
                "여유" -> LOW
                "보통" -> MODERATE
                "약간 붐빔" -> HIGH
                "붐빔" -> SEVERE
                else -> UNKNOWN
            }
        }
    }
}