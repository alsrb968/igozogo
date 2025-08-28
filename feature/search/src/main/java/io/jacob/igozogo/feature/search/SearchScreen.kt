package io.jacob.igozogo.feature.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.jacob.igozogo.core.design.component.*
import io.jacob.igozogo.core.design.icon.IgozogoIcons
import io.jacob.igozogo.core.design.theme.IgozogoTheme
import io.jacob.igozogo.core.design.tooling.DevicePreviews
import io.jacob.igozogo.core.model.Place
import io.jacob.igozogo.core.model.Story
import io.jacob.igozogo.core.testing.data.categoryTestData
import io.jacob.igozogo.core.testing.data.placeTestData
import io.jacob.igozogo.core.testing.data.recentSearchTestData
import io.jacob.igozogo.core.testing.data.storyTestData
import kotlinx.coroutines.flow.collectLatest
import io.jacob.igozogo.core.design.R as designR

@Composable
internal fun SearchRoute(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    onPlaceClick: (Place) -> Unit,
    onStoryClick: (Story) -> Unit,
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
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

                is SearchEffect.NavigateToPlaceDetails -> onPlaceClick(effect.place)

                is SearchEffect.NavigateToStoryDetails -> onStoryClick(effect.story)
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
        onStoryPlay = { /* TODO */ }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
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
    onStoryPlay: (Story) -> Unit = {}
) {
    IgozogoScaffold(
        modifier = modifier,
        title = stringResource(designR.string.core_design_search)
    ) { paddingValues, nestedScrollConnection ->

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .nestedScroll(nestedScrollConnection),
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
                        CategoriesColumns(
                            categories = state.categories,
                            onCategoryClicked = onCategoryClicked
                        )
                    }

                is SearchState.RecentSearchesDisplay -> {
                    item {
                        RecentSearchesColumn(
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
                        item {
                            SearchResultsColumn(
                                places = state.places,
                                stories = state.stories,
                                onPlaceClicked = onPlaceClicked,
                                onStoryClicked = onStoryClicked,
                                onStoryPlay = onStoryPlay
                            )
                        }
                    }
                }

                is SearchState.Error -> {
                    // todo: Show error state
                }
            }
        }
    }
}

@Composable
private fun CategoriesColumns(
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
private fun RecentSearchesColumn(
    modifier: Modifier = Modifier,
    recentSearches: List<String>,
    onRecentSearchClicked: (String) -> Unit,
    onRemoveRecentSearch: (String) -> Unit,
    onClearRecentSearches: () -> Unit
) {
    Column(
        modifier = modifier,
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
            modifier = Modifier.padding(horizontal = 8.dp)
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
private fun SearchResultsColumn(
    modifier: Modifier = Modifier,
    places: List<Place>,
    stories: List<Story>,
    onPlaceClicked: (Place) -> Unit,
    onStoryClicked: (Story) -> Unit,
    onStoryPlay: (Story) -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        places.forEach { place ->
            PlaceSearchItem(
                place = place,
                onClick = { onPlaceClicked(place) }
            )
        }

        stories.forEach { story ->
            StorySearchItem(
                story = story,
                onClick = { onStoryClicked(story) },
                onPlay = { onStoryPlay(story) }
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
private fun CategoriesColumnsPreview() {
    IgozogoTheme {
        CategoriesColumns(
            categories = categoryTestData,
            onCategoryClicked = {}
        )
    }
}

@DevicePreviews
@Composable
private fun RecentSearchesColumnPreview() {
    IgozogoTheme {
        RecentSearchesColumn(
            recentSearches = recentSearchTestData,
            onRecentSearchClicked = {},
            onRemoveRecentSearch = {},
            onClearRecentSearches = {}
        )
    }
}

@DevicePreviews
@Composable
private fun SearchResultsColumnPreview() {
    IgozogoTheme {
        SearchResultsColumn(
            places = placeTestData.take(5),
            stories = storyTestData.take(5),
            onPlaceClicked = {},
            onStoryClicked = {},
            onStoryPlay = {}
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