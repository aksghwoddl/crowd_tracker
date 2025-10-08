package com.lee.crowdtracker.core.domain

import com.lee.crowdtracker.core.data.dto.CityDataDto
import com.lee.crowdtracker.core.data.dto.CityDataResponseDto
import com.lee.crowdtracker.core.data.dto.ForecastPopulationDto
import com.lee.crowdtracker.core.data.dto.ResultInfoDto
import com.lee.crowdtracker.core.data.repository.SeoulCityDataRepository
import com.lee.crowdtracker.core.domain.beach.model.CityDataModel
import com.lee.crowdtracker.core.domain.beach.model.CongestionLevel
import com.lee.crowdtracker.core.domain.beach.usecase.citydata.GetCityDataUseCase
import com.lee.crowdtracker.library.test.base.BaseTest
import com.lee.crowdtracker.library.test.utils.shouldBe
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetCityDataUseCaseTest : BaseTest() {
    @MockK
    lateinit var seoulCityDataRepository: SeoulCityDataRepository

    private lateinit var useCase: GetCityDataUseCase

    override fun setup() {
        super.setup()
        useCase = GetCityDataUseCase(seoulCityDataRepository = seoulCityDataRepository)
    }

    @Test
    fun `정상 응답 테스트`() = runTest {
        coEvery {
            seoulCityDataRepository.getCityData(name = any())
        } returns Result.success(
            CityDataResponseDto(
                cityDataList = listOf(
                    CityDataDto(
                        areaName = "익선동",
                        areaCode = "POI116",
                        congestionLevel = "여유",
                        congestionMessage = "사람이 몰려있을 가능성이 낮고 붐빔은 거의 느껴지지 않아요. 도보 이동이 자유로워요.",
                        populationMin = "6500",
                        populationMax = "7000",
                        malePopulationRate = "58.3",
                        femalePopulationRate = "41.7",
                        populationRate0 = "0.6",
                        populationRate10 = "3.6",
                        populationRate20 = "27.5",
                        populationRate30 = "20.2",
                        populationRate40 = "16.8",
                        populationRate50 = "15.8",
                        populationRate60 = "10.0",
                        populationRate70 = "5.7",
                        residentPopulationRate = "26.2",
                        nonResidentPopulationRate = "73.8",
                        replaceYn = "N",
                        populationTime = "2025-08-13 21:50",
                        forecastYn = "Y",
                        forecastPopulation = listOf(
                            ForecastPopulationDto(
                                forecastTime = "2025-08-13 23:00",
                                forecastCongestionLevel = "여유",
                                forecastPopulationMin = "6000",
                                forecastPopulationMax = "6500"
                            ),
                            ForecastPopulationDto(
                                forecastTime = "2025-08-14 00:00",
                                forecastCongestionLevel = "여유",
                                forecastPopulationMin = "4500",
                                forecastPopulationMax = "5000"
                            ),
                            ForecastPopulationDto(
                                forecastTime = "2025-08-14 01:00",
                                forecastCongestionLevel = "여유",
                                forecastPopulationMin = "4000",
                                forecastPopulationMax = "4500"
                            )
                        )
                    )
                ),
                result = ResultInfoDto(
                    code = "INFO-000",
                    message = "정상 처리되었습니다."
                )
            )
        )

        useCase(name = "익선동").collect {
            it shouldBe listOf(
                CityDataModel(
                    name = "익선동",
                    congestionLevel = CongestionLevel.LOW,
                    congestionMessage = "사람이 몰려있을 가능성이 낮고 붐빔은 거의 느껴지지 않아요. 도보 이동이 자유로워요.",
                )
            )
        }
    }

    @Test
    fun `Exception 케이스 테스트`() = runTest {
        coEvery { seoulCityDataRepository.getCityData(name = any()) } returns Result.failure(exception = Exception("Boom"))

        useCase(name = "some_name").collect {
            it shouldBe emptyList()
        }
    }
}