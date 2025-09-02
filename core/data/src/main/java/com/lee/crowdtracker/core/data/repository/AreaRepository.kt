package com.lee.crowdtracker.core.data.repository

import com.lee.crowdtracker.core.data.db.csv.CsvDownloadEntity
import com.lee.crowdtracker.core.data.dto.AreaDto
import kotlinx.coroutines.flow.Flow

interface AreaRepository {
    suspend fun readAreaFromCsv(): List<AreaDto>
    suspend fun insertDownloadArea(
        areaId: String,
        category: String,
        no: Int,
        name: String,
        englishName: String
    )

    fun getAreaListFromName(name: String): Flow<List<CsvDownloadEntity>>

    suspend fun getDownloadedAreaList(): List<CsvDownloadEntity>
}