package com.lee.crowdtracker.core.domain.beach.usecase.area

import com.lee.crowdtracker.core.data.datastore.PreferenceDataStore
import com.lee.crowdtracker.core.data.repository.AreaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DownloadAreaByCsvUseCase @Inject constructor(
    private val areaRepository: AreaRepository,
    private val preferenceDataStore: PreferenceDataStore
) {
    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        if (preferenceDataStore.isDownloadArea.first()) return@withContext // 이미 다운로드 받은 경우 return
        areaRepository.readAreaFromCsv().forEach {
            areaRepository.insertDownloadArea(
                areaId = it.areaId,
                category = it.category,
                no = it.no,
                name = it.name,
                englishName = it.englishName
            )
        }
        preferenceDataStore.setIsDownloadArea(isDownload = true)
    }
}