package com.lee.crowdtracker.search

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lee.crowdtracker.core.domain.beach.model.Area
import com.lee.crowdtracker.libray.design.component.CdInputBox
import com.lee.crowdtracker.libray.design.theme.CDTheme
import com.lee.crowdtracker.search.component.AreaList
import com.lee.crowdtracker.search.model.SearchScreenEffect
import com.lee.crowdtracker.search.model.SearchScreenEvent
import com.lee.crowdtracker.search.model.SearchScreenState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Composable
fun SearchRoute(
    onShowSnackBar: suspend (String, String?) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            when (it) {
                is SearchScreenEffect.ShowSnackBar -> {
                    onShowSnackBar(it.message, it.actionLabel)
                }
            }
        }
    }

    SearchScreen(
        state = state,
        onTextChange = {
            viewModel.handleEvent(
                SearchScreenEvent.OnChangeSearchText(
                    text = it
                )
            )
        },
        onKeyboardActionSearch = {
            viewModel.handleEvent(SearchScreenEvent.OnKeyboardActionSearch)
        },
        onClickArea = {
            viewModel.handleEvent(SearchScreenEvent.OnClickArea(area = it))
        }
    )
}

@Composable
internal fun SearchScreen(
    onTextChange: (String) -> Unit,
    onKeyboardActionSearch: () -> Unit,
    onClickArea: (Area) -> Unit,
    state: SearchScreenState,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CdInputBox(
            modifier = Modifier.fillMaxWidth(),
            text = state.searchText,
            placeholder = "검색어를 입력하세요.",
            isShowTrailingIcon = true,
            trailingIcon = Icons.Default.Search,
            onTextChange = onTextChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    Log.d("TAG", "CdInputBox: done")
                    onKeyboardActionSearch()
                    keyboardController?.hide()
                }
            ),
        )
        AreaList(
            areas = state.areaList.toPersistentList(),
            onClickItem = {
                onClickArea(it)
                keyboardController?.hide()
            }
        )
    }
}

@Composable
@Preview(
    showSystemUi = true
)
private fun SearchScreenPreview() {
    CDTheme {
        SearchScreen(
            state = SearchScreenState(
                areaList = persistentListOf(
                    Area(
                        no = 7,
                        name = "홍대 관광특구",
                        category = "관광특구",
                    ),
                    Area(
                        no = 53,
                        name = "홍대입구역(2호선)",
                        category = "인구밀집지역",
                    ),
                )
            ),
            onTextChange = {},
            onKeyboardActionSearch = {},
            onClickArea = {}
        )
    }
}