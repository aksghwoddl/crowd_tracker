package com.lee.crowdtracker.search

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lee.crowdtracker.core.domain.beach.model.AreaModel
import com.lee.crowdtracker.libray.design.component.CdInputBox
import com.lee.crowdtracker.libray.design.theme.CDTheme
import com.lee.crowdtracker.search.component.AreaList
import com.lee.crowdtracker.search.component.CityContentsDialog
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

private const val TAG = "SearchRoute"

@Composable
fun SearchRoute(
    onShowSnackBar: suspend (String, String?) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchUiState by viewModel.searchUiState.collectAsStateWithLifecycle()
    val cityDataUiState by viewModel.cityDataUiState.collectAsStateWithLifecycle()

    SearchScreen(
        searchUiState = searchUiState,
        cityDataUiState = cityDataUiState,
        onTextChange = viewModel::onQueryChange,
        onClickArea = viewModel::onAreaClick,
        onDialogDismiss = viewModel::onCityContentsDialogDismiss
    )
}

@Composable
internal fun SearchScreen(
    onTextChange: (String) -> Unit,
    onClickArea: (AreaModel) -> Unit,
    onDialogDismiss: () -> Unit,
    searchUiState: SearchUiState,
    cityDataUiState: CityDataUiState,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val inputBoxFocusRequester = remember { FocusRequester() }
    var searchText by rememberSaveable { mutableStateOf("") }

    when (cityDataUiState) {
        is CityDataUiState.Success -> {
            CityContentsDialog(
                name = cityDataUiState.name,
                level = cityDataUiState.level,
                message = cityDataUiState.message,
                onDismiss = onDialogDismiss
            )
        }

        CityDataUiState.Loading -> {}
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CdInputBox(
            modifier = Modifier
                .focusRequester(inputBoxFocusRequester)
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            text = searchText,
            placeholder = "검색어를 입력하세요.",
            isShowTrailingIcon = true,
            trailingIcon = Icons.Default.Search,
            onTextChange = {
                searchText = it
                onTextChange(it)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    Log.d("TAG", "CdInputBox: done")
                    keyboardController?.hide()
                }
            ),
        )

        when (searchUiState) {
            SearchUiState.Loading -> {
                SearchLoading()
            }

            is SearchUiState.Empty -> {
                EmptyResult(
                    onRetryButtonClick = {
                        inputBoxFocusRequester.requestFocus()
                    }
                )
            }

            is SearchUiState.Success -> {
                AreaList(
                    areaModels = searchUiState.areaModelList.toPersistentList(),
                    onClickItem = {
                        onClickArea(it)
                        keyboardController?.hide()
                    }
                )
            }

            is SearchUiState.Error -> {
            }

        }
    }
}

@Composable
private fun SearchLoading(
    modifier: Modifier = Modifier
) {
    PlaceholderScreen(
        modifier = modifier,
        title = "검색어를 입력해보세요",
        message = "예시 : 홍대 맛집"
    )
}

@Composable
private fun EmptyResult(
    modifier: Modifier = Modifier,
    onRetryButtonClick: () -> Unit
) {
    PlaceholderScreen(
        modifier = modifier,
        title = "검색 결과가 없습니다",
        message = "다른 키워드로 검색해 보세요",
        buttonText = "다시 검색",
        onButtonClick = onRetryButtonClick
    )
}

@Composable
private fun PlaceholderScreen(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Search,
    title: String,
    message: String? = null,
    buttonText: String? = null,
    onButtonClick: (() -> Unit) = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
            message?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            }
            buttonText?.let { text ->
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = { onButtonClick.invoke() }) {
                    Text(text)
                }
            }
        }
    }
}


@Composable
@Preview(
    showSystemUi = true
)
private fun SearchResultScreenPreview() {
    CDTheme {
        SearchScreen(
            searchUiState = SearchUiState.Success(
                areaModelList = persistentListOf(
                    AreaModel(
                        no = 7,
                        name = "홍대 관광특구",
                        category = "관광특구",
                    ),
                    AreaModel(
                        no = 53,
                        name = "홍대입구역(2호선)",
                        category = "인구밀집지역",
                    ),
                )
            ),
            cityDataUiState = CityDataUiState.Loading,
            onTextChange = {},
            onClickArea = {},
            onDialogDismiss = {}
        )
    }
}

@Composable
@Preview(
    showSystemUi = true
)
private fun SearchLoadingScreenPreview() {
    CDTheme {
        SearchScreen(
            searchUiState = SearchUiState.Loading,
            cityDataUiState = CityDataUiState.Loading,
            onTextChange = {},
            onClickArea = {},
            onDialogDismiss = {}
        )
    }
}

@Composable
@Preview(
    showSystemUi = true
)
private fun SearchEmptyScreenPreview() {
    CDTheme {
        SearchScreen(
            searchUiState = SearchUiState.Empty,
            cityDataUiState = CityDataUiState.Loading,
            onTextChange = {},
            onClickArea = {},
            onDialogDismiss = {}
        )
    }
}

@Composable
@Preview(
    showSystemUi = true
)
private fun SearcScreenCityContentsDialogPreview() {
    CDTheme {
        SearchScreen(
            searchUiState = SearchUiState.Success(
                areaModelList = persistentListOf(
                    AreaModel(
                        no = 7,
                        name = "홍대 관광특구",
                        category = "관광특구",
                    ),
                    AreaModel(
                        no = 53,
                        name = "홍대입구역(2호선)",
                        category = "인구밀집지역",
                    ),
                )
            ),
            cityDataUiState = CityDataUiState.Success(
                name = "",
                level = "붐빔",
                message = "붐비고 있습니다."
            ),
            onTextChange = {},
            onClickArea = {},
            onDialogDismiss = {}
        )
    }
}