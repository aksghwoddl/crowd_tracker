package com.lee.crowdtracker.core.data.repository

import com.lee.crowdtracker.core.data.db.csv.CsvDownloadEntity
import com.lee.crowdtracker.core.data.dto.AreaDto

interface AreaRepository {
    suspend fun readAreaFromCsv(): List<AreaDto>
    suspend fun insertDownloadArea(
        areaId: String,
        category: String,
        no: Int,
        name: String,
        englishName: String
    )

    suspend fun getDownloadedAreaList() : List<CsvDownloadEntity>
}