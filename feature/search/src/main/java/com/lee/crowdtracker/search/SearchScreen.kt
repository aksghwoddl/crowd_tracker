package com.lee.crowdtracker.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lee.crowdtracker.libray.design.component.CdInputBox
import com.lee.crowdtracker.libray.design.theme.CDTheme
import com.lee.crowdtracker.search.model.SearchScreenEvent
import com.lee.crowdtracker.search.model.SearchScreenState

@Composable
fun SearchRoute(
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SearchScreen(
        state = state,
        onTextChange = {
            viewModel.handleEvent(
                SearchScreenEvent.OnChangeSearchText(
                    text = it
                )
            )
        },
        onSearchButtonClick = {
            viewModel.handleEvent(SearchScreenEvent.OnSearchButtonClick)
        }
    )
}

@Composable
internal fun SearchScreen(
    onTextChange: (String) -> Unit,
    onSearchButtonClick: () -> Unit,
    state: SearchScreenState,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CdInputBox(
            modifier = Modifier.fillMaxWidth(),
            text = state.searchText,
            isShowTrailingIcon = true,
            trailingIcon = Icons.Default.Search,
            onTrailingIconClick = onSearchButtonClick,
            onTextChange = onTextChange,
        )
    }
}

@Composable
@Preview
private fun SearchScreenPreview() {
    CDTheme {
        SearchScreen(
            state = SearchScreenState(),
            onTextChange = {},
            onSearchButtonClick = {}
        )
    }
}