package com.lee.crowdtracker.core.data.impl.repository

import com.lee.crowdtracker.core.data.dto.CityDataResponseDto
import com.lee.crowdtracker.core.data.impl.BuildConfig
import com.lee.crowdtracker.core.data.impl.service.SeoulOpenApiService
import com.lee.crowdtracker.core.data.repository.SeoulCityDataRepository
import jakarta.inject.Inject

class SeoulCityDataRepositoryImpl @Inject constructor(
    private val seoulOpenApiService: SeoulOpenApiService,
) : SeoulCityDataRepository {
    override suspend fun getCityData(name: String): Result<CityDataResponseDto> = runCatching {
        seoulOpenApiService.getCityData(
            apiKey = BuildConfig.SEOUL_OPEN_API_KEY,
            areaName = name
        )
    }
}