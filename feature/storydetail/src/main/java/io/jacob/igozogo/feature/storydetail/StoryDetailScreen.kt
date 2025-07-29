package io.jacob.igozogo.feature.storydetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.jacob.igozogo.core.design.component.LoadingWheel
import io.jacob.igozogo.core.design.component.StateImage
import io.jacob.igozogo.core.design.component.StoryAction
import io.jacob.igozogo.core.design.icon.IgozogoIcons
import io.jacob.igozogo.core.design.theme.IgozogoTheme
import io.jacob.igozogo.core.design.tooling.DevicePreviews
import io.jacob.igozogo.core.design.tooling.PreviewPlace
import io.jacob.igozogo.core.design.tooling.PreviewStory
import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.core.domain.model.Story

@Composable
fun StoryDetailRoute(
    modifier: Modifier = Modifier,
    viewModel: StoryDetailViewModel = hiltViewModel(),
    onPlaceClick: (Place) -> Unit,
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (val s = state) {
        is StoryDetailState.Loading -> LoadingWheel(modifier = modifier)
        is StoryDetailState.Error -> {}
        is StoryDetailState.Success -> {
            StoryDetailScreen(
                modifier = modifier,
                place = s.place,
                story = s.story,
                onPlaceClick = onPlaceClick,
                onBackClick = onBackClick,
                onPlayClick = viewModel::play,
                onShowSnackbar = onShowSnackbar,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryDetailScreen(
    modifier: Modifier = Modifier,
    place: Place,
    story: Story,
    onPlaceClick: (Place) -> Unit,
    onBackClick: () -> Unit,
    onPlayClick: (Story) -> Unit,
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val screenWidth = this.maxWidth
        val screenHeight = this.maxHeight
        val aspectRatio = 4f / 3f
        val imageHeight = screenWidth / aspectRatio
        val sheetMinHeight = screenHeight - imageHeight + 50.dp

        val sheetState = rememberBottomSheetScaffoldState(
            bottomSheetState = rememberStandardBottomSheetState(
                initialValue = SheetValue.PartiallyExpanded
            )
        )

        BottomSheetScaffold(
            modifier = Modifier.fillMaxSize(),
            scaffoldState = sheetState,
            sheetPeekHeight = sheetMinHeight,
            sheetDragHandle = {},
            sheetContainerColor = MaterialTheme.colorScheme.surface,
            sheetContent = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = sheetMinHeight) // 실제 이미지 높이 반영
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                            .padding(top = 32.dp)
                    ) {
                        item {
                            PlaceAddress(
                                address = place.fullAddress,
                                modifiedTime = story.humanReadableModifiedTime
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = story.audioTitle,
                                style = MaterialTheme.typography.headlineMedium,
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                modifier = Modifier
                                    .clickable {
                                        onPlaceClick(place)
                                    },
                                text = place.title,
                                style = MaterialTheme.typography.titleLarge,
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            StoryAction(
                                story = story,
                                onAdd = {},
                                onDownload = {},
                                onShare = {},
                                onPlay = onPlayClick
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = story.script,
                                style = MaterialTheme.typography.bodyLarge,
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            },
        ) {
            StateImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(aspectRatio),
                imageUrl = story.imageUrl,
                contentDescription = story.title,
            )

            IconButton(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                        shape = CircleShape
                    ),
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = IgozogoIcons.Back,
                    contentDescription = "Back"
                )
            }
        }
    }
}

@Composable
private fun PlaceAddress(
    modifier: Modifier = Modifier,
    address: String,
    modifiedTime: String
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = IgozogoIcons.Place,
            contentDescription = "Location",
            tint = MaterialTheme.colorScheme.primary,
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = address,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = modifiedTime,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@DevicePreviews
@Composable
private fun StoryDetailScreenPreview() {
    IgozogoTheme {
        StoryDetailScreen(
            place = PreviewPlace,
            story = PreviewStory,
            onPlaceClick = {},
            onBackClick = {},
            onPlayClick = {},
            onShowSnackbar = { _, _ -> true },
        )
    }
}