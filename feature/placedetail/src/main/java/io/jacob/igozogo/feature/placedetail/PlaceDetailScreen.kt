package io.jacob.igozogo.feature.placedetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.jacob.igozogo.core.design.component.LoadingWheel
import io.jacob.igozogo.core.design.component.StateImage
import io.jacob.igozogo.core.design.component.StoryItem
import io.jacob.igozogo.core.design.icon.IgozogoIcons
import io.jacob.igozogo.core.design.theme.IgozogoTheme
import io.jacob.igozogo.core.design.tooling.DevicePreviews
import io.jacob.igozogo.core.model.Place
import io.jacob.igozogo.core.model.Story
import io.jacob.igozogo.core.testing.data.placeTestData
import io.jacob.igozogo.core.testing.data.storyTestData

@Composable
fun PlaceDetailRoute(
    modifier: Modifier = Modifier,
    viewModel: PlaceDetailViewModel = hiltViewModel(),
    onStoryClick: (Story) -> Unit,
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (val s = state) {
        is PlaceDetailState.Loading -> LoadingWheel(modifier = modifier)
        is PlaceDetailState.Error -> {}
        is PlaceDetailState.Success -> {
            PlaceDetailScreen(
                modifier = modifier,
                place = s.place,
                stories = s.stories,
                onStoryClick = onStoryClick,
                onBackClick = onBackClick,
                onPlayClick = viewModel::play,
                onShowSnackbar = onShowSnackbar
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class,)
@Composable
fun PlaceDetailScreen(
    modifier: Modifier = Modifier,
    place: Place,
    stories: List<Story>,
    onStoryClick: (Story) -> Unit,
    onBackClick: () -> Unit,
    onPlayClick: (List<Story>) -> Unit,
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
) {
    val listState = rememberLazyListState()

    val showTopBar by remember {
        derivedStateOf {
            // 첫 번째 아이템이 화면에 안 보일 때만 true
            val firstVisibleItem = listState.firstVisibleItemIndex > 0
            val offsetPastFirst = listState.firstVisibleItemIndex == 0 &&
                    listState.firstVisibleItemScrollOffset > 700
            firstVisibleItem || offsetPastFirst
        }
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(bottom = 0.dp),
        topBar = {
            TopAppBar(
                modifier = Modifier,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = if (showTopBar) 1f else 0f)
                ),
                title = {
                    AnimatedVisibility(
                        visible = showTopBar,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = place.title,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector = IgozogoIcons.Back,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = paddingValues.calculateTopPadding())
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    StateImage(
                        modifier = Modifier
                            .size(200.dp)
                            .clip(shape = RoundedCornerShape(8.dp)),
                        imageUrl = place.imageUrl,
                        contentDescription = place.title,
                    )

                    Text(
                        modifier = Modifier
                            .padding(vertical = 16.dp),
                        text = place.title,
                        style = MaterialTheme.typography.headlineMedium,
                    )

                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.outlineVariant,
                                    shape = CircleShape
                                ),
                            onClick = { /* TODO */ }
                        ) {
                            Icon(
                                imageVector = IgozogoIcons.Download,
                                contentDescription = null
                            )
                        }

                        IconButton(
                            modifier = Modifier
                                .size(70.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.outlineVariant,
                                    shape = CircleShape
                                ),
                            onClick = { onPlayClick(stories) }
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(48.dp),
                                imageVector = IgozogoIcons.Play,
                                contentDescription = null
                            )
                        }

                        IconButton(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.outlineVariant,
                                    shape = CircleShape
                                ),
                            onClick = { /* TODO */ }
                        ) {
                            Icon(
                                imageVector = IgozogoIcons.Share,
                                contentDescription = null
                            )
                        }
                    }
                }
            }

            items(
                count = stories.size,
                key = { stories[it].storyLangId },
            ) { index ->
                stories[index].let { story ->
                    StoryItem(
                        story = story,
                        onClick = { onStoryClick(story) }
                    )
                }
            }
        }
    }
}

@DevicePreviews
@Composable
private fun PlaceDetailScreenPreview() {
    IgozogoTheme {
        PlaceDetailScreen(
            place = placeTestData.first(),
            stories = storyTestData,
            onStoryClick = {},
            onBackClick = {},
            onPlayClick = {},
            onShowSnackbar = { _, _ -> true }
        )
    }
}