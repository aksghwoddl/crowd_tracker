package com.lee.crowdtracker.core.data.repository

import com.lee.crowdtracker.core.data.dto.BeachListDTO

interface BeachRepository {
    /**
     * 해수욕장 혼잡도 가져오기
     * **/
    suspend fun getBeachCongestion(): BeachListDTO
}