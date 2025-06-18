package io.jacob.igozogo.feature.placedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.jacob.igozogo.core.design.component.LoadingWheel
import io.jacob.igozogo.core.design.component.StateImage
import io.jacob.igozogo.core.design.component.StoryItem
import io.jacob.igozogo.core.design.theme.IgozogoTheme
import io.jacob.igozogo.core.design.tooling.DevicePreviews
import io.jacob.igozogo.core.design.tooling.PreviewPlace
import io.jacob.igozogo.core.design.tooling.previewStoriesLazyPagingItems
import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.core.domain.model.Story

@Composable
fun PlaceDetailRoute(
    modifier: Modifier = Modifier,
    viewModel: PlaceDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (val s = state) {
        is PlaceDetailUiState.Loading -> LoadingWheel(modifier = modifier)
        is PlaceDetailUiState.Error -> {}
        is PlaceDetailUiState.Success -> {
            PlaceDetailScreen(
                modifier = modifier,
                place = s.place,
                stories = s.stories.collectAsLazyPagingItems(),
                onBackClick = onBackClick,
                onShowSnackbar = onShowSnackbar
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceDetailScreen(
    modifier: Modifier = Modifier,
    place: Place,
    stories: LazyPagingItems<Story>,
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    val scrollBehavior = rememberTopAppBarState()
    val appBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(scrollBehavior)

    val collapsedFraction by remember {
        derivedStateOf { appBarScrollBehavior.state.collapsedFraction }
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(bottom = 0.dp),
        topBar = {
            TopAppBar(
                modifier = Modifier,
//                    .nestedScroll(appBarScrollBehavior.nestedScrollConnection)
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent, // 배경을 투명하게
                    scrolledContainerColor = Color.Transparent, // 스크롤 시에도 투명하게 유지
                ),
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = place.title,
                        textAlign = TextAlign.Center,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
//                scrollBehavior = appBarScrollBehavior
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
//                .nestedScroll(appBarScrollBehavior.nestedScrollConnection)
            ,
        ) {
            item {
                Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding()))
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    StateImage(
                        modifier = Modifier
                            .size(200.dp)
                            .clip(shape = RoundedCornerShape(8.dp)),
                        imageUrl = place.imageUrl,
                        contentDescription = place.title,
                    )
                }
            }

            stickyHeader {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((paddingValues.calculateTopPadding())),
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.secondary)
                            .padding(8.dp),
                        text = place.title,
                    )
                }

            }

            items(stories.itemCount) { index ->
                stories[index]?.let { story ->
                    StoryItem(
                        story = story,
                        onClick = { /* TODO */ }
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
            place = PreviewPlace,
            stories = previewStoriesLazyPagingItems(),
            onBackClick = {},
            onShowSnackbar = { _, _ -> true }
        )
    }
}