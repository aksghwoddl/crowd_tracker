package com.lee.crowdtracker.core.domain.beach.usecase.citydata

import com.lee.crowdtracker.core.data.repository.SeoulCityDataRepository
import com.lee.crowdtracker.core.domain.beach.mapper.toCityDataModel
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCityDataUseCase @Inject constructor(
    private val seoulCityDataRepository: SeoulCityDataRepository
) {
    operator fun invoke(name: String) = flow {
        emit(seoulCityDataRepository.getCityData(name = name).getOrNull()?.cityDataList?.map {
            it.toCityDataModel()
        } ?: emptyList())
    }
}