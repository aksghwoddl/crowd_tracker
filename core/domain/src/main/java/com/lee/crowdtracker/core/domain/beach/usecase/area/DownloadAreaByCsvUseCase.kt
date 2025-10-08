package com.lee.crowdtracker.core.domain.beach.usecase.area

import com.lee.crowdtracker.core.data.datasource.preference.PreferenceDataSource
import com.lee.crowdtracker.core.data.repository.AreaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DownloadAreaByCsvUseCase @Inject constructor(
    private val areaRepository: AreaRepository,
    private val preferenceDataSource: PreferenceDataSource,
) {
    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        // 이미 다운로드 받은 경우 return
        if (preferenceDataSource.getIsDownloadArea().first()) return@withContext
        areaRepository.readAreaFromCsv().forEach {
            areaRepository.insertDownloadArea(
                areaId = it.areaId,
                category = it.category,
                no = it.no,
                name = it.name,
                englishName = it.englishName
            )
        }
        preferenceDataSource.setIsDownloadArea(download = true)
    }
}