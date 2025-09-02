package com.lee.crowdtracker.core.domain.beach.mapper

import com.lee.crowdtracker.core.data.dto.CityDataDto
import com.lee.crowdtracker.core.domain.beach.model.CityDataModel

fun CityDataDto.toCityDataModel() = CityDataModel(
    name = areaName,
    congestionLevel = congestionLevel,
    congestionMessage = congestionMessage,
)