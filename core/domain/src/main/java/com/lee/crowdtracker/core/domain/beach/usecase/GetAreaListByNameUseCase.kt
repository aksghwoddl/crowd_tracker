package com.lee.crowdtracker.core.domain.beach.usecase

import com.lee.crowdtracker.core.data.db.csv.CsvDownloadEntity
import com.lee.crowdtracker.core.data.repository.AreaRepository
import com.lee.crowdtracker.core.domain.beach.mapper.toArea
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAreaListByNameUseCase @Inject constructor(
    private val areaRepository: AreaRepository
) {
    operator fun invoke(name: String) =
        areaRepository.getAreaListFromName(name = name).map { csvDownloadEntities ->
            csvDownloadEntities.toAreaList()
        }

    private fun List<CsvDownloadEntity>.toAreaList() = this.map { it.toArea() }
}