package com.lee.crowdtracker.search

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.lee.crowdtracker.core.domain.beach.model.AreaModel
import com.lee.crowdtracker.core.domain.beach.model.CityDataModel
import com.lee.crowdtracker.core.domain.beach.model.CongestionLevel
import com.lee.crowdtracker.core.domain.beach.usecase.area.GetAreaListByNameUseCase
import com.lee.crowdtracker.core.domain.beach.usecase.citydata.GetCityDataUseCase
import com.lee.crowdtracker.library.test.base.BaseTest
import com.lee.crowdtracker.library.test.utils.shouldBe
import com.lee.crowdtracker.search.model.Area
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SearchViewModelTest : BaseTest() {

    @MockK(relaxed = true)
    private lateinit var getAreaListByNameUseCase: GetAreaListByNameUseCase

    @MockK(relaxed = true)
    private lateinit var getCityDataUseCase: GetCityDataUseCase

    private lateinit var viewModel: SearchViewModel

    override fun setup() {
        super.setup()
        viewModel = SearchViewModel(
            getAreaListByNameUseCase = getAreaListByNameUseCase,
            getCityDataUseCase = getCityDataUseCase,
            savedStateHandle = SavedStateHandle()
        )
    }

    @Test
    fun `검색어 입력 시 상태 테스트`() = runTest {
        val areaList = listOf(
            AreaModel(
                no = 1,
                name = "해운대",
                category = "beach",
            )
        )

        every { getAreaListByNameUseCase(any()) } returns flowOf(areaList)

        viewModel.searchUiState.test {
            awaitItem() shouldBe SearchUiState.Loading
            viewModel.onQueryChange("해")

            val state = awaitItem()
            (state is SearchUiState.Success) shouldBe true

            if (state is SearchUiState.Success) {
                state.areaList.size shouldBe 1
                state.areaList.first().name shouldBe "해운대"
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `지역을 선택시 혼잡도 상태 테스트`() = runTest {
        val area = Area(
            no = 1,
            name = "해운대",
            category = "beach",
        )

        val cityDataModelList = listOf(
            CityDataModel(
                name = area.name,
                congestionLevel = CongestionLevel.MODERATE,
                congestionMessage = "보통",
            )
        )

        every { getCityDataUseCase(name = area.name) } returns flowOf(cityDataModelList)

        viewModel.cityDataUiState.test {
            awaitItem() shouldBe CityDataUiState.Loading
            viewModel.onAreaClick(area)

            val state = awaitItem()

            (state is CityDataUiState.Success) shouldBe true
            if(state is CityDataUiState.Success) {
                state.name shouldBe "해운대"
                state.level shouldBe CongestionLevel.MODERATE
                state.message shouldBe "보통"
            }
            cancelAndIgnoreRemainingEvents()
        }
    }
}
