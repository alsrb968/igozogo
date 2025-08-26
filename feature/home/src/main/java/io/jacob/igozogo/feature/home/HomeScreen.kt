package io.jacob.igozogo.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.jacob.igozogo.core.design.R
import io.jacob.igozogo.core.design.component.*
import io.jacob.igozogo.core.design.foundation.NestedScrollLazyColumn
import io.jacob.igozogo.core.design.theme.IgozogoTheme
import io.jacob.igozogo.core.design.tooling.DevicePreviews
import io.jacob.igozogo.core.domain.usecase.FeedSection
import io.jacob.igozogo.core.model.Place
import io.jacob.igozogo.core.model.Story
import io.jacob.igozogo.core.testing.data.categoryTestData
import io.jacob.igozogo.core.testing.data.placeTestData
import io.jacob.igozogo.core.testing.data.storyTestData
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onPlaceClick: (Place) -> Unit,
    onStoryClick: (Story) -> Unit,
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is HomeEffect.Synced ->
                    onShowSnackbar(
                        context.getString(R.string.core_design_place_sync_completed),
                        "OK"
                    )

                is HomeEffect.NavigateToCategoryDetails -> {}
                is HomeEffect.NavigateToPlaceDetails -> onPlaceClick(effect.place)
                is HomeEffect.NavigateToStoryDetails -> onStoryClick(effect.story)
            }
        }
    }

    when (val s = state) {
        is HomeState.Loading -> LoadingWheel(modifier = modifier)
        is HomeState.Error -> {}
        is HomeState.Success -> {
            HomeScreen(
                modifier = modifier,
                feedSections = s.feedSections,
                onCategoryClick = { category ->
                    viewModel.sendAction(HomeAction.ClickCategory(category))
                },
                onPlaceClick = { place ->
                    viewModel.sendAction(HomeAction.ClickPlace(place))
                },
                onStoryClick = { story ->
                    viewModel.sendAction(HomeAction.ClickStory(story))
                }
            )
        }
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    feedSections: List<FeedSection>,
    onCategoryClick: (String) -> Unit,
    onPlaceClick: (Place) -> Unit,
    onStoryClick: (Story) -> Unit,
) {
    NestedScrollLazyColumn(
        modifier = modifier,
        state = rememberLazyListState()
    ) {
        items(
            count = feedSections.size,
            key = { feedSections[it].hashCode() },
        ) { index ->
            when (val section = feedSections[index]) {
                is FeedSection.Categories -> {
                    SectionHeader(
                        modifier = Modifier
                            .padding(bottom = 16.dp),
                        text = stringResource(R.string.core_design_category)
                    ) {
                        CategoriesRows(
                            categories = section.categories,
                            onItemClick = onCategoryClick
                        )
                    }
                }

                is FeedSection.Places -> {
                    SectionHeader(
                        modifier = Modifier
                            .padding(bottom = 16.dp),
                        text = stringResource(R.string.core_design_place), onMore = { }
                    ) {
                        PlacesRow(
                            places = section.places,
                            isBookmarked = { false },
                            onBookmarkToggle = { },
                            onItemClick = onPlaceClick
                        )
                    }
                }

                is FeedSection.Stories -> {
                    SectionHeader(
                        modifier = Modifier
                            .padding(bottom = 16.dp),
                        text = stringResource(R.string.core_design_story), onMore = { }
                    ) {
                        StoriesRow(
                            stories = section.stories,
                            onItemClick = onStoryClick
                        )
                    }
                }
            }
        }

        item {
            Text(text = "End of list")
        }
    }
}

@Composable
private fun CategoriesRows(
    modifier: Modifier = Modifier,
    categories: List<String>,
    onItemClick: (String) -> Unit,
) {
    val padding = 16.dp

    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp + padding),
        horizontalArrangement = Arrangement.spacedBy(padding),
        verticalArrangement = Arrangement.spacedBy(padding),
        contentPadding = PaddingValues(horizontal = padding)
    ) {
        items(
            count = categories.size,
            key = { categories[it] },
        ) { index ->
            CategoryItem(
                modifier = Modifier.width(200.dp),
                category = categories[index],
                onClick = { onItemClick(categories[index]) }
            )
        }
    }
}

@Composable
private fun PlacesRow(
    modifier: Modifier = Modifier,
    places: List<Place>,
    isBookmarked: (Place) -> Boolean,
    onBookmarkToggle: (Place) -> Unit,
    onItemClick: (Place) -> Unit,
) {
    val padding = 16.dp

    LazyRow(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(padding),
        contentPadding = PaddingValues(horizontal = padding)
    ) {
        items(
            count = places.size,
            key = { places[it].placeLangId },
        ) { index ->
            places[index].let { place ->
                PlaceItem(
                    place = place,
                    isBookmarked = isBookmarked(place),
                    onBookmarkToggle = { onBookmarkToggle(place) },
                    onClick = { onItemClick(place) }
                )
            }
        }
    }
}

@Composable
private fun StoriesRow(
    modifier: Modifier = Modifier,
    stories: List<Story>,
    onItemClick: (Story) -> Unit,
) {
    val padding = 16.dp

    LazyRow(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(padding),
        contentPadding = PaddingValues(horizontal = padding)
    ) {
        items(
            count = stories.size,
            key = { stories[it].storyLangId },
        ) { index ->
            StoryItem(
                modifier = Modifier
                    .width(250.dp),
                story = stories[index],
                onClick = onItemClick
            )
        }
    }
}

@DevicePreviews
@Composable
private fun HomeScreenPreview() {
    IgozogoTheme {
        HomeScreen(
            feedSections = listOf(
                FeedSection.Categories(categoryTestData),
                FeedSection.Places(placeTestData),
                FeedSection.Stories(storyTestData),
            ),
            onCategoryClick = {},
            onPlaceClick = {},
            onStoryClick = {}
        )
    }
}