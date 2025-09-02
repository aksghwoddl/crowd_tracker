package com.lee.crowdtracker.core.data.repository

import com.lee.crowdtracker.core.data.dto.CityDataResponseDto

interface SeoulCityDataRepository {
    suspend fun getCityData(name: String): Result<CityDataResponseDto>
}