package com.lee.crowdtracker.core.domain

import com.lee.crowdtracker.core.data.db.csv.CsvDownloadEntity
import com.lee.crowdtracker.core.data.repository.AreaRepository
import com.lee.crowdtracker.core.domain.beach.model.Area
import com.lee.crowdtracker.core.domain.beach.usecase.GetAreaListByNameUseCase
import com.lee.crowdtracker.library.test.base.BaseTest
import com.lee.crowdtracker.library.test.utils.shouldBe
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
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
        } returns listOf(
            CsvDownloadEntity(
                areaId = "",
                category = "관광특구",
                no = 7,
                name = "홍대입구",
                englishName = "Hongdae"
            )
        )

        with(useCase(name = "name")) {
            this shouldBe listOf(
                Area(
                    no = 7,
                    name = "홍대입구",
                    category = "관광특구",
                )
            )
        }
    }
}