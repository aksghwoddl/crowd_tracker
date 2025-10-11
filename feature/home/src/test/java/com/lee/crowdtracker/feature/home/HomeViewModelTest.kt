package com.lee.crowdtracker.feature.home

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.lee.crowdtracker.core.domain.beach.model.CongestionLevel
import com.lee.crowdtracker.core.domain.beach.model.CrowdDataModel
import com.lee.crowdtracker.core.domain.beach.usecase.home.GetCrowdDataUseCase
import com.lee.crowdtracker.library.test.base.BaseTest
import com.lee.crowdtracker.library.test.utils.shouldBe
import com.lee.crowdtracker.libray.navermap.NaverMapSdkController
import com.naver.maps.geometry.LatLng
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Test

class HomeViewModelTest : BaseTest() {

    @MockK
    private lateinit var getCrowdDataUseCase: GetCrowdDataUseCase

    @MockK
    private lateinit var naverMapSdkController: NaverMapSdkController

    private lateinit var viewModel: HomeViewModel

    override fun setup() {
        super.setup()
        viewModel = HomeViewModel(
            getCrowdDataUseCase = getCrowdDataUseCase,
            naverMapSdkController = naverMapSdkController,
            savedStateHandle = SavedStateHandle()
        )
    }

    @Test
    fun `초기 로드 성공 테스트`() = runTest {
        val crowdData = listOf(
            CrowdDataModel(
                id = 1,
                name = "해운대",
                category = "beach",
                congestionLevel = CongestionLevel.MODERATE,
                congestionMessage = "보통",
            )
        )

        val latLng = LatLng(
            35.1587,
            129.1604
        )

        coEvery { getCrowdDataUseCase() } returns crowdData
        coEvery { naverMapSdkController.getLatLngByName(any(), any()) } returns latLng

        viewModel.uiState.test {
            awaitItem() shouldBe HomeUiState.Loading
            val state = awaitItem()
            (state is HomeUiState.Success) shouldBe true

            if (state is HomeUiState.Success) {
                state.crowdMarkerData.size shouldBe 1
                state.crowdMarkerData.first().name shouldBe "해운대"
                state.crowdMarkerData.first().level shouldBe CongestionLevel.MODERATE
                state.crowdMarkerData.first().latLng shouldBe latLng
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `마커 정보 없을때 테스트`() = runTest {
        coEvery { getCrowdDataUseCase() } returns emptyList()

        viewModel.uiState.test {
            val state = awaitItem()
            (state is HomeUiState.Error) shouldBe true

            if (state is HomeUiState.Error) {
                state.message shouldBe "마커정보가 비어있습니다."
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `마커 정보 받는중 예외 처리 테스트`() = runTest {
        coEvery { getCrowdDataUseCase() } throws IllegalStateException("알수 없는 에러 발생")
        viewModel.uiState.test {
            val state = awaitItem()
            (state is HomeUiState.Error) shouldBe true

            if (state is HomeUiState.Error) {
                state.message shouldBe "마커 정보를 불러오는중 에러 발생"
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `retry 함수 테스트`() = runTest {
        val firstData = emptyList<CrowdDataModel>()
        val secondData = listOf(
            CrowdDataModel(id = 1, name = "송도", category = "beach")
        )
        val latLng = LatLng(
            35.1587,
            129.1604
        )
        coEvery { getCrowdDataUseCase() } returnsMany listOf(firstData, secondData)
        coEvery { naverMapSdkController.getLatLngByName(any(), any()) } returns latLng

        viewModel.uiState.test {
            val firstState = awaitItem()
            (firstState is HomeUiState.Error) shouldBe true

            viewModel.onRetry()

            awaitItem() shouldBe HomeUiState.Loading

            val secondState = awaitItem()
            (secondState is HomeUiState.Success) shouldBe true

            if (secondState is HomeUiState.Success) {
                secondState.crowdMarkerData.first().name shouldBe "송도"
                secondState.crowdMarkerData.first().latLng shouldBe latLng
            }
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 2) { getCrowdDataUseCase() }
    }
}
