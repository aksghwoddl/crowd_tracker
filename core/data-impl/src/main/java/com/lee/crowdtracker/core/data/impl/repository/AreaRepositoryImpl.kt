package com.lee.crowdtracker.core.data.impl.repository

import android.content.Context
import com.lee.crowdtracker.core.data.db.csv.CsvDownloadDao
import com.lee.crowdtracker.core.data.db.csv.CsvDownloadEntity
import com.lee.crowdtracker.core.data.dto.AreaDto
import com.lee.crowdtracker.core.data.repository.AreaRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

class AreaRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val csvDownloadDao: CsvDownloadDao,
) : AreaRepository {
    override suspend fun readAreaFromCsv(): List<AreaDto> {
        val bufferedReader = BufferedReader(InputStreamReader(context.assets.open("area_list.csv")))
        return bufferedReader.useLines { lines ->
            lines.drop(1)
                .mapNotNull { line ->
                    val data = line.split(",")
                    if (data.size >= 5) {
                        AreaDto(
                            category = data[0].trim(),
                            no = data[1].trim().toIntOrNull() ?: 0,
                            areaId = data[2].trim(),
                            name = data[3].trim(),
                            englishName = data[4].trim()
                        )
                    } else null
                }.toList()
        }
    }

    override suspend fun insertDownloadArea(
        areaId: String,
        category: String,
        no: Int,
        name: String,
        englishName: String
    ) {
        csvDownloadDao.insertArea(
            area = CsvDownloadEntity(
                areaId = areaId,
                category = category,
                no = no,
                name = name,
                englishName = englishName
            )
        )
    }

    override fun getAreaListFromName(name: String): Flow<List<CsvDownloadEntity>> {
        return csvDownloadDao.getAreaByName(name = name)
    }

    override suspend fun getDownloadedAreaList() = withContext(Dispatchers.IO) {
        csvDownloadDao.getAreaList()
    }
}