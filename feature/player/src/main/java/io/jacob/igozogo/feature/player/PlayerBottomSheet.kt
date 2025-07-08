package io.jacob.igozogo.feature.player

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.jacob.igozogo.core.design.component.SquareCard
import io.jacob.igozogo.core.design.component.StateImage
import io.jacob.igozogo.core.design.theme.IgozogoTheme
import io.jacob.igozogo.core.design.tooling.DevicePreviews
import io.jacob.igozogo.core.design.tooling.PreviewPlace
import io.jacob.igozogo.core.design.tooling.PreviewStory
import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.core.domain.model.PlayerProgress
import io.jacob.igozogo.core.domain.model.Story
import io.jacob.igozogo.core.domain.util.RepeatMode
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
    val snackbarHostState = remember { SnackbarHostState() }

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
        sheetMaxWidth = Dp.Infinity,
        dragHandle = null,
        containerColor = Color.Transparent,
        scrimColor = Color.Transparent,
        contentWindowInsets = { WindowInsets(0) },
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = true,
        ),
    ) {
        Card(
            modifier = Modifier
                .padding(WindowInsets.statusBars.asPaddingValues())
        ) {
            PlayerScreen(
                modifier = Modifier,
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
            .fillMaxSize()
//            .gradientBackground(
//                ratio = 1f,
//                startColor = dominantColor,
//                endColor = MaterialTheme.colorScheme.background
//            ),
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState,
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    ControlPanelTop(
                        modifier = Modifier,
                        onCollapse = onCollapse
                    )

                    Spacer(modifier = Modifier.height(100.dp))

                    SquareCard {
                        StateImage(
                            modifier = Modifier
                                .fillMaxSize(),
                            imageUrl = nowPlaying.imageUrl,
                            contentDescription = nowPlaying.audioTitle
                        )
                    }

                    Spacer(modifier = Modifier.height(100.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .padding(start = 4.dp),
                    ) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .fillMaxWidth()
                                .padding(end = 50.dp)
                                .basicMarquee(),
                            text = nowPlaying.audioTitle,
                            style = MaterialTheme.typography.bodyLarge,
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
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )

                        IconButton(
                            modifier = Modifier
                                .align(Alignment.CenterEnd),
                            onClick = actions.onFavorite,
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
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

            }

//            item {
//                Spacer(modifier = Modifier.height(105.dp))
//                ArtistDetailsInfoCard()
//            }

//            item {
//                TrackDetailsInfoCard()
//            }
        }
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
                imageVector = Icons.Default.KeyboardArrowDown,
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
            text = playerProgress.formattedPosition,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 4.dp),
            text = playerProgress.formattedDuration,
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
            val icon = if (isShuffle) Icons.Default.ShuffleOn else Icons.Default.Shuffle
            val color =
                if (isShuffle) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
            Icon(
                imageVector = icon,
                contentDescription = "Shuffle",
                tint = color
            )
        }

        IconButton(
            onClick = actions.onPrevious
        ) {
            Icon(
                imageVector = Icons.Default.SkipPrevious,
                contentDescription = "Previous",
            )
        }

        IconButton(
            modifier = Modifier.size(56.dp),
            onClick = actions.onPlayOrPause
        ) {
            val icon = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow
            Icon(
                modifier = Modifier.size(56.dp),
                imageVector = icon,
                contentDescription = "Play",
            )
        }

        IconButton(
            onClick = actions.onNext
        ) {
            Icon(
                imageVector = Icons.Default.SkipNext,
                contentDescription = "Next",
            )
        }

        IconButton(
            onClick = actions.onRepeat
        ) {
            val icon = when (repeatMode) {
                RepeatMode.OFF -> Icons.Default.Repeat
                RepeatMode.ONE -> Icons.Default.RepeatOneOn
                RepeatMode.ALL -> Icons.Default.RepeatOn
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
            nowPlaying = PreviewStory,
            playerProgress = PlayerProgress(1000, 2000, 3000),
            isPlaying = true,
            isShuffle = false,
            repeatMode = RepeatMode.OFF,
            place = PreviewPlace,
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
                onSeekTo = {}
            ),
            onCollapse = {}
        )
    }
}