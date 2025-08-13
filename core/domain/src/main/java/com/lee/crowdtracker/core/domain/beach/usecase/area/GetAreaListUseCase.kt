package com.lee.crowdtracker.core.domain.beach.usecase.area

import com.lee.crowdtracker.core.data.repository.AreaRepository
import com.lee.crowdtracker.core.domain.beach.mapper.toArea
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAreaListUseCase @Inject constructor(
    private val areaRepository: AreaRepository
) {
    suspend operator fun invoke() = withContext(Dispatchers.Default) {
        areaRepository.getDownloadedAreaList().map { it.toArea() }
    }
}