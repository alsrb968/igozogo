package io.jacob.igozogo.feature.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.jacob.igozogo.core.design.component.StateImage
import io.jacob.igozogo.core.design.theme.IgozogoTheme
import io.jacob.igozogo.core.design.tooling.DevicePreviews
import io.jacob.igozogo.core.design.tooling.PreviewPlace
import io.jacob.igozogo.core.design.tooling.PreviewStory
import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.core.domain.model.PlayerProgress
import io.jacob.igozogo.core.domain.model.Story


val PLAYER_BAR_HEIGHT = 70.dp

@Composable
fun PlayerMiniBar(
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isShowPlayer by viewModel.isShowPlayer.collectAsStateWithLifecycle()

//    LaunchedEffect(Unit) {
//        viewModel.effect.collectLatest { effect ->
//            when (effect) {
//                is PlayerUiEffect.ShowToast -> onShowSnackBar(effect.message)
//            }
//        }
//    }

//            val context = LocalContext.current
//            LaunchedEffect(s.nowPlaying.imageUrl) {
//                val color = extractDominantColorFromUrl(context, s.nowPlaying.imageUrl)
//                viewModel.sendIntent(PlayerUiIntent.SetDominantColor(color))
//            }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = state is PlayerState.Ready,
            modifier = Modifier.align(Alignment.BottomCenter), // 위치 유지
            enter = slideInVertically(
                initialOffsetY = { it }, // 자기 키만큼 아래에서 등장
                animationSpec = tween(300)
            ),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(300)
            )
        ) {
            val s = state as? PlayerState.Ready ?: return@AnimatedVisibility

            PlayerMiniBar(
                modifier = modifier,
                nowPlaying = s.nowPlaying,
                progress = s.progress,
                isPlaying = s.isPlaying,
                place = s.place,
                isFavorite = false,
                actions = PlayerMiniBarActions(
                    onPlayOrPause = { viewModel.sendAction(PlayerAction.PlayOrPause) },
                    onFavorite = {},
                ),
                onExpand = { viewModel.sendAction(PlayerAction.ClickPlayer) },
            )
        }
    }

//    if (isShowPlayer) {
//        PlayerBottomSheet()
//    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PlayerMiniBar(
    modifier: Modifier = Modifier,
    nowPlaying: Story,
    progress: PlayerProgress,
    isPlaying: Boolean,
    place: Place?,
    isFavorite: Boolean,
    actions: PlayerMiniBarActions,
    onExpand: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(PLAYER_BAR_HEIGHT)
            .padding(horizontal = 6.dp)
            .padding(bottom = 6.dp),
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = onExpand
    ) {
        Box {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(5.dp))
                ) {
                    StateImage(
                        modifier = Modifier
                            .fillMaxSize(),
                        imageUrl = nowPlaying.imageUrl,
                        contentDescription = nowPlaying.audioTitle
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp)
                        .padding(horizontal = 10.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .basicMarquee(),
                        text = nowPlaying.audioTitle,
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .basicMarquee(),
                        text = place?.title ?: "",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                ControlBar(
                    modifier = Modifier
                        .padding(end = 16.dp),
                    isFavorite = isFavorite,
                    isPlaying = isPlaying,
                    actions = actions,
                )
            }

            LinearProgressIndicator(
                progress = { progress.bufferedRatio },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .padding(horizontal = 10.dp)
                    .align(Alignment.BottomCenter),
                color = MaterialTheme.colorScheme.outlineVariant,
//                gapSize = 0.dp,
            )
            LinearProgressIndicator(
                progress = { progress.positionRatio },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .padding(horizontal = 10.dp)
                    .align(Alignment.BottomCenter),
//                color = MaterialTheme.colorScheme.onSurface,
                trackColor = Color.Transparent,
//                gapSize = 0.dp,
            )
        }
    }
}

@Composable
fun ControlBar(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    isPlaying: Boolean,
    actions: PlayerMiniBarActions,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        IconButton(
            modifier = Modifier
                .size(36.dp),
            onClick = actions.onFavorite,
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.CheckCircle else Icons.Outlined.AddCircleOutline,
                contentDescription = "Favorite",
                tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }

        IconButton(
            modifier = Modifier
                .size(36.dp),
            onClick = actions.onPlayOrPause,
        ) {
            Icon(
                modifier = Modifier
                    .size(32.dp),
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = "Play/Pause",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

data class PlayerMiniBarActions(
    val onPlayOrPause: () -> Unit,
    val onFavorite: () -> Unit,
)

@DevicePreviews
@Composable
private fun PlayerMiniBarPreview() {
    IgozogoTheme {
        PlayerMiniBar(
            nowPlaying = PreviewStory,
            progress = PlayerProgress(
                position = 30,
                buffered = 60,
                duration = 100,
            ),
            isPlaying = true,
            place = PreviewPlace,
            isFavorite = false,
            actions = PlayerMiniBarActions(
                onPlayOrPause = {},
                onFavorite = {},
            ),
            onExpand = {},
        )
    }
}