package com.lee.crowdtracker.core.domain

import com.lee.crowdtracker.core.data.dto.BeachDTO
import com.lee.crowdtracker.core.data.dto.BeachListDTO
import com.lee.crowdtracker.core.data.repository.BeachRepository
import com.lee.crowdtracker.core.domain.beach.model.Congestion
import com.lee.crowdtracker.core.domain.beach.usecase.GetBeachCongestionListUseCase
import com.lee.crowdtracker.library.test.base.BaseTest
import com.lee.crowdtracker.library.test.utils.shouldBe
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetBeachCongestionListUseCaseTest : BaseTest() {
    private lateinit var useCase: GetBeachCongestionListUseCase
    @MockK
    lateinit var beachRepository: BeachRepository

    override fun setup() {
        super.setup()
        useCase = GetBeachCongestionListUseCase(beachRepository = beachRepository)
    }

    @Test
    fun `해변 목록 정상적으로 가져오는지 테스트`() = runTest {
        coEvery {
            beachRepository.getBeachCongestion()
        } returns BeachListDTO(
            beach0 = BeachDTO(
                poiNm = "해변1",
                congestion = "1"
            ),
            beach1 =  BeachDTO(
                poiNm = "해변2",
                congestion = "2"
            ),
            beach2 =  BeachDTO(
                poiNm = "해변3",
                congestion = "3"
            )
        )

       with(useCase()) {
           size shouldBe 3
           get(0).name shouldBe "해변1"
           get(0).congestion shouldBe Congestion.LOW
       }
    }
}