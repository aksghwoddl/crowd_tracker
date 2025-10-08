package com.lee.crowdtracker.core.domain

import com.lee.crowdtracker.core.data.datasource.preference.PreferenceDataSource
import com.lee.crowdtracker.core.data.datastore.PreferenceDataStore
import com.lee.crowdtracker.core.data.dto.AreaDto
import com.lee.crowdtracker.core.data.repository.AreaRepository
import com.lee.crowdtracker.core.domain.beach.usecase.area.DownloadAreaByCsvUseCase
import com.lee.crowdtracker.library.test.base.BaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DownloadAreaByCsvUseCaseTest : BaseTest() {
    @MockK
    lateinit var areaRepository: AreaRepository

    @MockK
    lateinit var preferenceDataSource: PreferenceDataSource

    private lateinit var useCase: DownloadAreaByCsvUseCase

    override fun setup() {
        super.setup()
        useCase = DownloadAreaByCsvUseCase(
            areaRepository = areaRepository,
            preferenceDataSource = preferenceDataSource,
        )
    }

    @Test
    fun `이미 다운로드를 받은 경우 테스트`() = runTest {
        coEvery { preferenceDataSource.getIsDownloadArea() } returns flowOf(true)
        useCase()
        coVerify(exactly = 0) { areaRepository.readAreaFromCsv() }
        coVerify(exactly = 0) {
            areaRepository.insertDownloadArea(
                areaId = any(),
                category = any(),
                no = any(),
                name = any(),
                englishName = any()
            )
        }
        coVerify(exactly = 0) { preferenceDataSource.setIsDownloadArea(download = any()) }
    }

    @Test
    fun `아직 다운로드를 받지 않은 경우 테스트`() = runTest {
        val mockData = listOf(
            AreaDto(
                category = "관광특구",
                no = 1,
                areaId = "POI001",
                name = "강남 MICE 관광특구",
                englishName = "Gangnam MICE Special Tourist Zone"
            ),
            AreaDto(
                category = "관광특구",
                no = 2,
                areaId = "POI002",
                name = "동대문 관광특구",
                englishName = "Dongdaemun Special Tourist Zone"
            )
        )

        coEvery { preferenceDataSource.getIsDownloadArea() } returns flowOf(false)
        coEvery { areaRepository.readAreaFromCsv() } returns mockData
        useCase()

        mockData.forEach {
            coVerify {
                areaRepository.insertDownloadArea(
                    category = it.category,
                    areaId = it.areaId,
                    name = it.name,
                    englishName = it.englishName,
                    no = it.no

                )
            }
        }

        coVerify { preferenceDataSource.setIsDownloadArea(true) }

    }
}