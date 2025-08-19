package io.jacob.igozogo.feature.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.jacob.igozogo.core.design.component.LoadingWheel
import io.jacob.igozogo.core.design.foundation.NestedScrollLazyColumn
import io.jacob.igozogo.core.design.icon.IgozogoIcons
import io.jacob.igozogo.core.design.theme.IgozogoTheme
import io.jacob.igozogo.core.design.tooling.DevicePreviews
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SearchRoute(
    modifier: Modifier = Modifier,
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is SearchEffect.ShowToast -> {
                    onShowSnackbar(
                        effect.message,
                        "OK"
                    )
                }

                is SearchEffect.NavigateToPlaceDetails -> {
                    // Handle navigation to place details
                }

                is SearchEffect.NavigateToStoryDetails -> {
                    // Handle navigation to story details
                }
            }
        }
    }

    SearchScreen(
        modifier = modifier,
        state = state,
        searchQuery = searchQuery,
        onSearchQueryChanged = { viewModel.sendAction(SearchAction.Search(it)) },
        onFocusedChanged = { viewModel.sendAction(SearchAction.UpdateFocus(it)) },
        onClear = { viewModel.sendAction(SearchAction.Clear) }
    )
}

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    state: SearchState = SearchState.Loading,
    searchQuery: String = "",
    onSearchQueryChanged: (String) -> Unit = {},
    onFocusedChanged: (Boolean) -> Unit = {},
    onClear: () -> Unit = {},
) {
    NestedScrollLazyColumn(
        modifier = modifier,
        state = rememberLazyListState()
    ) {
        stickyHeader {
            SearchBar(
                modifier = Modifier,
                query = searchQuery,
                onQueryChanged = onSearchQueryChanged,
                onFocusChanged = onFocusedChanged,
                onClear = onClear
            )
        }

        when (state) {
            is SearchState.Loading -> item {
                LoadingWheel(modifier = Modifier.fillMaxSize())
            }

            is SearchState.CategoriesDisplay -> {
                // todo: Show categories
            }

            is SearchState.RecentSearchesDisplay -> {
                // todo: Show recent searches
            }

            is SearchState.Success -> {
                if (state.isEmpty) {
                    // todo: Show search results empty state
                } else {
                    // todo: Show search results
                }
            }

            is SearchState.Error -> {
                // todo: Show error state
            }
        }
    }
}


@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChanged: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit,
    onClear: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        placeholder = {
            Text(
                text = "장소나 이야기를 검색해보세요",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = IgozogoIcons.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(
                        imageVector = IgozogoIcons.Close,
                        contentDescription = "Clear",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(28.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(28.dp),
                ambientColor = Color.Black.copy(alpha = 0.05f),
                spotColor = Color.Black.copy(alpha = 0.05f)
            )
            .onFocusChanged { onFocusChanged(it.isFocused) },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        )
    )
}

@DevicePreviews
@Composable
private fun SearchBarPreview() {
    IgozogoTheme {
        SearchBar(
            query = "Search query",
            onQueryChanged = {},
            onFocusChanged = {},
            onClear = {}
        )
    }
}