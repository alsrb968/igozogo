@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package io.jacob.igozogo.feature.player

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.jacob.igozogo.core.design.component.LargeIconButton
import io.jacob.igozogo.core.design.component.MediumIconButton
import io.jacob.igozogo.core.design.component.SquareCard
import io.jacob.igozogo.core.design.component.StateImage
import io.jacob.igozogo.core.design.icon.IgozogoIcons
import io.jacob.igozogo.core.design.theme.IgozogoTheme
import io.jacob.igozogo.core.design.tooling.DevicePreviews
import io.jacob.igozogo.core.design.util.toMediaTimeFormat
import io.jacob.igozogo.core.model.Place
import io.jacob.igozogo.core.model.PlayerProgress
import io.jacob.igozogo.core.model.RepeatMode
import io.jacob.igozogo.core.model.Story
import io.jacob.igozogo.core.testing.data.placeTestData
import io.jacob.igozogo.core.testing.data.storyTestData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerBottomSheet(
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel = hiltViewModel(),
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is PlayerEffect.NavigateToPlaceDetail -> {}
                is PlayerEffect.NavigateToStoryDetail -> {}
                is PlayerEffect.ShowPlayerBottomSheet -> {}
                is PlayerEffect.HidePlayerBottomSheet -> {
                    sheetState.hide()
                }
            }
        }
    }

    val s = state as? PlayerState.Ready ?: return

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = {
            viewModel.sendAction(PlayerAction.CollapsePlayer)
        },
        sheetState = sheetState,
        dragHandle = null,
        scrimColor = Color.Transparent,
        contentWindowInsets = { WindowInsets(0) },
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = true,
        ),
    ) {
        PlayerScreen(
            modifier = Modifier
                .padding(WindowInsets.statusBars.asPaddingValues()),
            nowPlaying = s.content.nowPlaying,
            playerProgress = s.content.playerProgress,
            isPlaying = s.meta.isPlaying,
            isShuffle = s.meta.isShuffle,
            repeatMode = s.meta.repeatMode,
            place = s.place,
            isFavorite = false,
//                dominantColor = s.dominantColor,
            actions = PlayerScreenActions(
                isFavorite = { flowOf(false)/*viewModel::isFavoriteTrack*/ },
                onFavorite = {},
                onFavoriteIndex = {},
                onPlayOrPause = { viewModel.sendAction(PlayerAction.PlayOrPause) },
                onPlayIndex = { viewModel.sendAction(PlayerAction.PlayIndex(it)) },
                onPrevious = { viewModel.sendAction(PlayerAction.Previous) },
                onNext = { viewModel.sendAction(PlayerAction.Next) },
                onShuffle = { viewModel.sendAction(PlayerAction.Shuffle) },
                onRepeat = { viewModel.sendAction(PlayerAction.Repeat) },
                onSeekTo = { viewModel.sendAction(PlayerAction.SeekTo(it)) },
                onSeekBackward = { viewModel.sendAction(PlayerAction.SeekBackward) },
                onSeekForward = { viewModel.sendAction(PlayerAction.SeekForward) },
            ),
            onCollapse = {
                scope.launch {
                    sheetState.hide()
                    viewModel.sendAction(PlayerAction.CollapsePlayer)
                }
            },
        )
    }
}

@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier,
    nowPlaying: Story,
    playerProgress: PlayerProgress,
    isPlaying: Boolean,
    isShuffle: Boolean,
    repeatMode: RepeatMode,
    place: Place,
    isFavorite: Boolean,
//    dominantColor: Color,
    actions: PlayerScreenActions,
    onCollapse: () -> Unit,
) {
    val listState = rememberLazyListState()

    Box(
        modifier = modifier
            .padding(horizontal = 16.dp)
//            .gradientBackground(
//                ratio = 1f,
//                startColor = dominantColor,
//                endColor = MaterialTheme.colorScheme.background
//            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            ControlPanelTop(
                modifier = Modifier,
                onCollapse = onCollapse
            )

            Spacer(modifier = Modifier.height(20.dp))

            SquareCard(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                StateImage(
                    modifier = Modifier
                        .fillMaxSize(),
                    imageUrl = nowPlaying.imageUrl,
                    contentDescription = nowPlaying.audioTitle
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(start = 4.dp),
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .fillMaxWidth()
                        .padding(end = 50.dp)
                        .basicMarquee(),
                    text = nowPlaying.audioTitle,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .padding(end = 50.dp)
                        .basicMarquee(),
                    text = place.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )

                IconButton(
                    modifier = Modifier
                        .align(Alignment.CenterEnd),
                    onClick = actions.onFavorite,
                ) {
                    Icon(
                        imageVector = if (isFavorite) IgozogoIcons.Bookmark else IgozogoIcons.BookmarkBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            ControlPanelProgress(
                modifier = Modifier
                    .fillMaxWidth(),
                nowPlaying = nowPlaying,
                playerProgress = playerProgress,
                actions = actions
            )

            ControlPanelBottom(
                modifier = Modifier
                    .fillMaxWidth(),
                isPlaying = isPlaying,
                isShuffle = isShuffle,
                repeatMode = repeatMode,
                actions = actions
            )

            Spacer(modifier = Modifier.weight(1f))
        }
//        Text(
//            text = nowPlaying.script,
//            style = MaterialTheme.typography.bodyMedium,
//        )

    }
}

data class PlayerScreenActions(
    val isFavorite: (String) -> Flow<Boolean>,
    val onFavorite: () -> Unit,
    val onFavoriteIndex: (Int) -> Unit,
    val onPlayOrPause: () -> Unit,
    val onPlayIndex: (Int) -> Unit,
    val onPrevious: () -> Unit,
    val onNext: () -> Unit,
    val onShuffle: () -> Unit,
    val onRepeat: () -> Unit,
    val onSeekTo: (Long) -> Unit,
    val onSeekBackward: () -> Unit,
    val onSeekForward: () -> Unit
)

@Composable
fun ControlPanelTop(
    modifier: Modifier = Modifier,
    onCollapse: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        IconButton(
            modifier = Modifier
                .size(36.dp)
                .align(Alignment.CenterStart),
            onClick = onCollapse
        ) {
            Icon(
                modifier = Modifier.size(36.dp),
                imageVector = IgozogoIcons.Down,
                contentDescription = "Down",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Now Playing",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Artist Name",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ControlPanelProgress(
    modifier: Modifier = Modifier,
    nowPlaying: Story,
    playerProgress: PlayerProgress,
    actions: PlayerScreenActions,
) {
    var position by remember { mutableFloatStateOf(playerProgress.positionRatio) }

    LaunchedEffect(playerProgress.position) {
        position = playerProgress.positionRatio
    }

    Box(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Slider(
            value = position,
            onValueChange = { value ->
                position = value
            },
            onValueChangeFinished = {
                actions.onSeekTo((position * playerProgress.duration).toLong())
            },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            ),
            thumb = {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .align(Alignment.Center)
                        .background(MaterialTheme.colorScheme.onSurface)
                )
            },
            track = { sliderState ->
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
                Box(
                    Modifier
                        .fillMaxWidth(sliderState.value)
                        .height(2.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurface)
                )
            }
        )

        Text(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 4.dp),
            text = playerProgress.position.toMediaTimeFormat(),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 4.dp),
            text = playerProgress.duration.toMediaTimeFormat(),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

    }
}

@Composable
fun ControlPanelBottom(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    isShuffle: Boolean,
    repeatMode: RepeatMode,
    actions: PlayerScreenActions,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = actions.onShuffle
        ) {
            val icon = if (isShuffle) IgozogoIcons.ShuffleOn else IgozogoIcons.Shuffle
            Icon(
                imageVector = icon,
                contentDescription = "Shuffle",
            )
        }

        MediumIconButton(
            onClick = actions.onSeekBackward,
            icon = IgozogoIcons.Replay10
        )

        LargeIconButton(
            onClick = actions.onPlayOrPause,
            isChecked = isPlaying,
            icon = IgozogoIcons.Play,
            checkedIcon = IgozogoIcons.Pause
        )

        MediumIconButton(
            onClick = actions.onSeekForward,
            icon = IgozogoIcons.Forward10
        )

        IconButton(
            onClick = actions.onRepeat
        ) {
            val icon = when (repeatMode) {
                RepeatMode.OFF -> IgozogoIcons.Repeat
                RepeatMode.ONE -> IgozogoIcons.RepeatOne
                RepeatMode.ALL -> IgozogoIcons.RepeatOn
            }
            Icon(
                imageVector = icon,
                contentDescription = "Repeat",
            )
        }
    }
}

@DevicePreviews
@Composable
private fun PlayerScreenPreview() {
    IgozogoTheme {
        PlayerScreen(
            nowPlaying = storyTestData.first(),
            playerProgress = PlayerProgress(1000, 2000, 3000),
            isPlaying = true,
            isShuffle = false,
            repeatMode = RepeatMode.OFF,
            place = placeTestData.first(),
            isFavorite = false,
//            dominantColor = Color.DarkGray,
            actions = PlayerScreenActions(
                isFavorite = { flowOf(false) },
                onFavorite = {},
                onFavoriteIndex = {},
                onPlayOrPause = {},
                onPlayIndex = {},
                onPrevious = {},
                onNext = {},
                onShuffle = {},
                onRepeat = {},
                onSeekTo = {},
                onSeekBackward = {},
                onSeekForward = {},
            ),
            onCollapse = {}
        )
    }
}