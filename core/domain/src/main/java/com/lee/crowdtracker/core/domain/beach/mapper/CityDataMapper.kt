package com.lee.crowdtracker.core.domain.beach.mapper

import com.lee.crowdtracker.core.data.dto.CityDataDto
import com.lee.crowdtracker.core.domain.beach.model.CityDataModel
import com.lee.crowdtracker.core.domain.beach.model.CongestionLevel

fun CityDataDto.toCityDataModel() = CityDataModel(
    name = areaName,
    congestionLevel = CongestionLevel.fromLabel(label = congestionLevel),
    congestionMessage = congestionMessage,
)