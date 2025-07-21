package com.lee.crowdtracker.core.domain.beach.usecase

import com.lee.crowdtracker.core.data.repository.AreaRepository
import com.lee.crowdtracker.core.domain.beach.mapper.toArea
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAreaListByNameUseCase @Inject constructor(
    private val areaRepository: AreaRepository
) {
    suspend operator fun invoke(name: String) = withContext(Dispatchers.Default) {
        areaRepository.getAreaListFromName(name = name).map {
            it.toArea()
        }
    }
}