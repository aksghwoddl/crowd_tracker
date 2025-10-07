package com.lee.crowdtracker.core.domain.beach.usecase.home

import com.lee.crowdtracker.core.domain.beach.model.CrowdDataModel
import com.lee.crowdtracker.core.domain.beach.usecase.area.GetAreaListUseCase
import com.lee.crowdtracker.core.domain.beach.usecase.citydata.GetCityDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetCrowdDataUseCase @Inject constructor(
    private val getAreaListUseCase: GetAreaListUseCase,
    private val getCityDataUseCase: GetCityDataUseCase,
) {
    suspend operator fun invoke() = withContext(Dispatchers.Default) {
        val areas = getAreaListUseCase()
        buildList(capacity = areas.size) {
            areas.forEach { area ->
                getCityDataUseCase(area.name).first().map { cityData ->
                    add(
                        CrowdDataModel(
                            id = area.no,
                            name = cityData.name,
                            category = area.category,
                            congestionLevel = cityData.congestionLevel,
                            congestionMessage = cityData.congestionMessage,
                        )
                    )

                }
            }
        }
    }
}