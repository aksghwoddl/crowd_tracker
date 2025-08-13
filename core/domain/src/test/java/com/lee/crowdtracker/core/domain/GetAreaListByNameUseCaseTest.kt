package com.lee.crowdtracker.core.domain

import com.lee.crowdtracker.core.data.db.csv.CsvDownloadEntity
import com.lee.crowdtracker.core.data.repository.AreaRepository
import com.lee.crowdtracker.core.domain.beach.model.AreaModel
import com.lee.crowdtracker.core.domain.beach.usecase.area.GetAreaListByNameUseCase
import com.lee.crowdtracker.library.test.base.BaseTest
import com.lee.crowdtracker.library.test.utils.shouldBe
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetAreaListByNameUseCaseTest : BaseTest() {

    @MockK
    lateinit var areaRepository: AreaRepository

    private lateinit var useCase: GetAreaListByNameUseCase

    override fun setup() {
        super.setup()
        useCase = GetAreaListByNameUseCase(areaRepository = areaRepository)
    }

    @Test
    fun `UseCase 반환값 테스트`() = runTest {
        coEvery {
            areaRepository.getAreaListFromName(name = any())
        } returns flowOf(
            listOf(
                CsvDownloadEntity(
                    areaId = "",
                    category = "관광특구",
                    no = 7,
                    name = "홍대입구",
                    englishName = "Hongdae"
                )
            )
        )

        with(useCase(name = "name")) {
            this.first() shouldBe listOf(
                AreaModel(
                    no = 7,
                    name = "홍대입구",
                    category = "관광특구",
                )
            )
        }
    }
}