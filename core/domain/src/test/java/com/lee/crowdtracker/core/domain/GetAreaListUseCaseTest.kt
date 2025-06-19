package com.lee.crowdtracker.core.domain

import com.lee.crowdtracker.core.data.db.csv.CsvDownloadEntity
import com.lee.crowdtracker.core.data.repository.AreaRepository
import com.lee.crowdtracker.core.domain.beach.usecase.GetAreaListUseCase
import com.lee.crowdtracker.library.test.base.BaseTest
import com.lee.crowdtracker.library.test.utils.shouldBe
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetAreaListUseCaseTest : BaseTest() {

    @MockK
    lateinit var areaRepository: AreaRepository

    private lateinit var useCase: GetAreaListUseCase

    override fun setup() {
        super.setup()
        useCase = GetAreaListUseCase(areaRepository = areaRepository)
    }

    @Test
    fun `UseCase 테스트`() = runTest {
        coEvery { areaRepository.getDownloadedAreaList() } returns listOf(
            CsvDownloadEntity(
                category = "관광특구",
                no = 1,
                areaId = "POI001",
                name = "강남 MICE 관광특구",
                englishName = "Gangnam MICE Special Tourist Zone"
            )
        )

        with(useCase()) {
            size shouldBe 1
            first().no shouldBe 1
            first().name shouldBe "강남 MICE 관광특구"
            first().category shouldBe "관광특구"
        }
    }
}