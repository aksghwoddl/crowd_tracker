package com.lee.crowdtracker.core.domain.beach.model

enum class Congestion {
    LOW, MEDIUM, HIGH
}

data class Beach(
    val name: String = "",
    var congestion: Congestion = Congestion.LOW
)