package io.jacob.igozogo.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
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
import io.jacob.igozogo.core.design.theme.IgozogoTheme
import io.jacob.igozogo.core.design.tooling.DevicePreviews
import io.jacob.igozogo.core.design.tooling.PreviewCategories
import io.jacob.igozogo.core.design.tooling.PreviewPlaces
import io.jacob.igozogo.core.design.tooling.PreviewStories
import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.core.domain.model.Story
import io.jacob.igozogo.core.domain.usecase.FeedSection
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onPlaceClick: (Place) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
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
                is HomeEffect.NavigateToStoryDetails -> {}
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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    feedSections: List<FeedSection>,
    onCategoryClick: (String) -> Unit,
    onPlaceClick: (Place) -> Unit,
    onStoryClick: (Story) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars),
        state = rememberLazyListState()
    ) {
        items(
            count = feedSections.size,
            key = { feedSections[it].hashCode() },
        ) { index ->
            val section = feedSections[index]
            when (section) {
                is FeedSection.Categories -> {
                    TitleTextItem(
                        modifier = Modifier
                            .padding(bottom = 16.dp),
                        text = stringResource(R.string.core_design_category)
                    ) {
                        ChipItemList(
                            chipItems = section.categories,
                            onItemClick = onCategoryClick
                        )
                    }
                }

                is FeedSection.Places -> {
                    TitleTextItem(
                        modifier = Modifier
                            .padding(bottom = 16.dp),
                        text = stringResource(R.string.core_design_place), onMore = { }
                    ) {
                        PlaceItemList(
                            places = section.places,
                            isBookmarked = { false },
                            onBookmarkToggle = { },
                            onItemClick = onPlaceClick
                        )
                    }
                }

                is FeedSection.Stories -> {
                    TitleTextItem(
                        modifier = Modifier
                            .padding(bottom = 16.dp),
                        text = stringResource(R.string.core_design_story), onMore = { }
                    ) {
                        StoryItemList(
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

@DevicePreviews
@Composable
private fun HomeScreenPreview() {
    IgozogoTheme {
        HomeScreen(
            feedSections = listOf(
                FeedSection.Categories(PreviewCategories),
                FeedSection.Places(PreviewPlaces),
                FeedSection.Stories(PreviewStories),
            ),
            onCategoryClick = {},
            onPlaceClick = {},
            onStoryClick = {}
        )
    }
}