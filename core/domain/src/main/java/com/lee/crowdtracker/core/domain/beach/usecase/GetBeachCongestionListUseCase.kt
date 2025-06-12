package com.lee.crowdtracker.core.domain.beach.usecase

import com.lee.crowdtracker.core.data.repository.BeachRepository
import com.lee.crowdtracker.core.domain.beach.mapper.toBeachList
import com.lee.crowdtracker.core.domain.beach.model.Beach
import javax.inject.Inject

class GetBeachCongestionListUseCase @Inject constructor(
    private val beachRepository: BeachRepository
) {
    suspend operator fun invoke(): List<Beach> {
        return beachRepository.getBeachCongestion().toBeachList()
    }
}