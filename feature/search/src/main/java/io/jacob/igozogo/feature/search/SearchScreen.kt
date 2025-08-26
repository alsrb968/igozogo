package io.jacob.igozogo.feature.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.jacob.igozogo.core.design.component.CategoryItem
import io.jacob.igozogo.core.design.component.LoadingWheel
import io.jacob.igozogo.core.design.component.SectionHeader
import io.jacob.igozogo.core.design.foundation.NestedScrollLazyColumn
import io.jacob.igozogo.core.design.icon.IgozogoIcons
import io.jacob.igozogo.core.design.theme.IgozogoTheme
import io.jacob.igozogo.core.design.tooling.DevicePreviews
import io.jacob.igozogo.core.model.Place
import io.jacob.igozogo.core.model.Story
import io.jacob.igozogo.core.testing.data.categoryTestData
import io.jacob.igozogo.core.testing.data.recentSearchTestData
import kotlinx.coroutines.flow.collectLatest
import io.jacob.igozogo.core.design.R as designR

@Composable
internal fun SearchRoute(
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

                is SearchEffect.NavigateToCategoryDetails -> {
                    // todo: Handle navigation to category details
                }

                is SearchEffect.NavigateToPlaceDetails -> {
                    // todo: Handle navigation to place details
                }

                is SearchEffect.NavigateToStoryDetails -> {
                    // todo: Handle navigation to story details
                }
            }
        }
    }

    SearchScreen(
        modifier = modifier,
        state = state,
        searchQuery = searchQuery,
        onSearchQueryChanged = { viewModel.sendAction(SearchAction.QueryChanged(it)) },
        onFocusedChanged = { viewModel.sendAction(SearchAction.FocusChanged(it)) },
        onSearch = { viewModel.sendAction(SearchAction.ClickSearch(it)) },
        onClearQuery = { viewModel.sendAction(SearchAction.ClearQuery) },
        onRemoveRecentSearch = { viewModel.sendAction(SearchAction.RemoveRecentSearch(it)) },
        onClearRecentSearches = { viewModel.sendAction(SearchAction.ClearRecentSearches) },
        onCategoryClicked = { viewModel.sendAction(SearchAction.ClickCategory(it)) },
        onRecentSearchClicked = { viewModel.sendAction(SearchAction.ClickRecentSearch(it)) },
        onPlaceClicked = { viewModel.sendAction(SearchAction.ClickPlace(it)) },
        onStoryClicked = { viewModel.sendAction(SearchAction.ClickStory(it)) },
    )
}

@Composable
private fun SearchScreen(
    modifier: Modifier = Modifier,
    state: SearchState = SearchState.Loading,
    searchQuery: String = "",
    onSearchQueryChanged: (String) -> Unit = {},
    onFocusedChanged: (Boolean) -> Unit = {},
    onSearch: (String) -> Unit = {},
    onClearQuery: () -> Unit = {},
    onRemoveRecentSearch: (String) -> Unit = {},
    onClearRecentSearches: () -> Unit = {},
    onCategoryClicked: (String) -> Unit = {},
    onRecentSearchClicked: (String) -> Unit = {},
    onPlaceClicked: (Place) -> Unit = {},
    onStoryClicked: (Story) -> Unit = {},
) {
    NestedScrollLazyColumn(
        modifier = modifier,
        state = rememberLazyListState()
    ) {
        stickyHeader {
            SearchBar(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                query = searchQuery,
                onQueryChanged = onSearchQueryChanged,
                onFocusChanged = onFocusedChanged,
                onSearch = onSearch,
                onClear = onClearQuery
            )
        }

        when (state) {
            is SearchState.Loading ->
                item {
                    LoadingWheel(modifier = Modifier.fillMaxSize())
                }

            is SearchState.CategoriesDisplay ->
                item {
                    CategoriesBody(
                        categories = state.categories,
                        onCategoryClicked = onCategoryClicked
                    )
                }

            is SearchState.RecentSearchesDisplay -> {
                item {
                    RecentSearchesBody(
                        recentSearches = state.recentSearches,
                        onRecentSearchClicked = onRecentSearchClicked,
                        onRemoveRecentSearch = onRemoveRecentSearch,
                        onClearRecentSearches = onClearRecentSearches
                    )
                }
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
private fun CategoriesBody(
    modifier: Modifier = Modifier,
    categories: List<String>,
    onCategoryClicked: (String) -> Unit
) {
    SectionHeader(
        modifier = modifier,
        text = stringResource(designR.string.core_design_category)
    ) {
        val padding = 8.dp

        FlowRow(
            modifier = Modifier.padding(padding),
            horizontalArrangement = Arrangement.spacedBy(padding),
            verticalArrangement = Arrangement.spacedBy(padding),
            maxItemsInEachRow = 2
        ) {
            categories.forEach { category ->
                CategoryItem(
                    modifier = Modifier.weight(1f),
                    category = category,
                    onClick = { onCategoryClicked(category) }
                )
            }
        }
    }
}

@Composable
private fun RecentSearchesBody(
    modifier: Modifier = Modifier,
    recentSearches: List<String>,
    onRecentSearchClicked: (String) -> Unit,
    onRemoveRecentSearch: (String) -> Unit,
    onClearRecentSearches: () -> Unit
) {
    SectionHeader(
        modifier = modifier,
        text = stringResource(designR.string.core_design_recent_search),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            recentSearches.forEach { recentSearch ->
                RecentSearchItem(
                    modifier = Modifier.fillMaxWidth(),
                    recentSearch = recentSearch,
                    onClick = onRecentSearchClicked,
                    onRemove = onRemoveRecentSearch
                )
            }
        }
    }
}

@Composable
private fun RecentSearchItem(
    modifier: Modifier = Modifier,
    recentSearch: String,
    onClick: (String) -> Unit,
    onRemove: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick(recentSearch) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = IgozogoIcons.History,
            contentDescription = "Recent Search Icon",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(end = 8.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = recentSearch,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = { onRemove(recentSearch) }) {
            Icon(
                imageVector = IgozogoIcons.Close,
                contentDescription = "Remove Recent Search",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChanged: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit,
    onSearch: (String) -> Unit,
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
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { onFocusChanged(it.isFocused) },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch(query)
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        )
    )
}

@DevicePreviews
@Composable
private fun CategoriesBodyPreview() {
    IgozogoTheme {
        CategoriesBody(
            categories = categoryTestData,
            onCategoryClicked = {}
        )
    }
}

@DevicePreviews
@Composable
private fun RecentSearchesBodyPreview() {
    IgozogoTheme {
        RecentSearchesBody(
            recentSearches = recentSearchTestData,
            onRecentSearchClicked = {},
            onRemoveRecentSearch = {},
            onClearRecentSearches = {}
        )
    }
}

@DevicePreviews
@Composable
private fun SearchBarPreview() {
    IgozogoTheme {
        SearchBar(
            query = "",
            onQueryChanged = {},
            onFocusChanged = {},
            onSearch = {},
            onClear = {}
        )
    }
}

@DevicePreviews
@Composable
private fun SearchScreenPreview() {
    IgozogoTheme {
        SearchScreen(
            state = SearchState.CategoriesDisplay(
                categories = categoryTestData
            )
        )
    }
}