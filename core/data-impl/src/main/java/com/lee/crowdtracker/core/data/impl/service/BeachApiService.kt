package com.lee.crowdtracker.core.data.impl.service

import com.lee.crowdtracker.core.data.common.DataConst
import com.lee.crowdtracker.core.data.dto.BeachListDTO
import retrofit2.http.GET

interface BeachApiService {
    @GET(DataConst.GET_BEACH_CONGESTION_URL)
    suspend fun getBeachCongestion() : BeachListDTO
}