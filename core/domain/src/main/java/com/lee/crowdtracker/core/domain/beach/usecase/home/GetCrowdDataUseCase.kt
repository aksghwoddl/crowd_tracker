package com.lee.crowdtracker.core.domain.beach.usecase.home

import com.lee.crowdtracker.core.domain.beach.model.CrowdDataModel
import com.lee.crowdtracker.core.domain.beach.usecase.area.GetAreaListUseCase
import com.lee.crowdtracker.core.domain.beach.usecase.citydata.GetCityDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetCrowdDataUseCase @Inject constructor(
    private val getAreaListUseCase: GetAreaListUseCase,
    private val getCityDataUseCase: GetCityDataUseCase,
) {
    suspend operator fun invoke() = withContext(Dispatchers.Default) {
        getAreaListUseCase().map { area ->
            async(Dispatchers.IO) {
                getCityDataUseCase(area.name).first().map { cityData ->
                    CrowdDataModel(
                        id = area.no,
                        name = cityData.name,
                        category = area.category,
                        congestionLevel = cityData.congestionLevel,
                        congestionMessage = cityData.congestionMessage,
                    )
                }
            }
        }
    }.awaitAll().flatten()
}
