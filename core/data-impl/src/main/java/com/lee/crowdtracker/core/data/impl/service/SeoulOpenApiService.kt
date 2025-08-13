package com.lee.crowdtracker.core.data.impl.service

import com.lee.crowdtracker.core.data.dto.CityDataResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface SeoulOpenApiService {
    @GET("{apiKey}/json/citydata_ppltn/1/108/{areaName}")
    suspend fun getCityData(
        @Path("apiKey") apiKey: String,
        @Path("areaName") areaName: String
    ): CityDataResponseDto
}