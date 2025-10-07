package com.lee.crowdtracker.core.domain.beach.model

enum class CongestionLevel(
    val label: String,
    val color: Long,
) {
    LOW(
        label = "여유",
        color = 0xFF2E7D32,
    ),
    MODERATE(
        label = "보통",
        color = 0xFF1976D2,
    ),
    HIGH(
        label = "약간 붐빔",
        color = 0xFFF57C00
    ),
    SEVERE(
        label = "붐빔",
        color = 0xFFC62828

    ),
    UNKNOWN(
        label = "",
        color = 0xFF9E9E9E
    ),
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