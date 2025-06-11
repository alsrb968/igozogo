package io.jacob.igozogo.core.design.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.jacob.igozogo.core.design.theme.IgozogoTheme
import io.jacob.igozogo.core.design.tooling.DevicePreviews
import io.jacob.igozogo.core.design.tooling.PreviewStory
import io.jacob.igozogo.core.domain.model.Story

@Composable
fun StoryItem(
    modifier: Modifier = Modifier,
    story: Story,
    onItemClick: (Story) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        onClick = { onItemClick(story) }
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StateImage(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(shape = RoundedCornerShape(8.dp)),
                    imageUrl = story.imageUrl,
                    contentDescription = story.audioTitle
                )

                Spacer(modifier = Modifier.width(8.dp))

                StoryContent(
                    story = story,
                )
            }

            StoryAction(
                story = story,
                onAdd = { /* TODO */ },
                onDownload = { /* TODO */ },
                onShare = { /* TODO */ },
                onPlay = { /* TODO */ }
            )
        }
    }
}

@Composable
fun StoryContent(
    modifier: Modifier = Modifier,
    story: Story
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = story.humanReadableModifiedTime,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = story.audioTitle,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = story.script,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun StoryAction(
    modifier: Modifier = Modifier,
    story: Story,
    onAdd: (Story) -> Unit,
    onDownload: (Story) -> Unit,
    onShare: (Story) -> Unit,
    onPlay: (Story) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            modifier = Modifier
                .size(32.dp),
            onClick = { onAdd(story) }
        ) {
            Icon(
                modifier = Modifier.size(ButtonDefaults.IconSize),
                imageVector = Icons.AutoMirrored.Filled.PlaylistAdd,
                contentDescription = "Add to playlist"
            )
        }

        IconButton(
            modifier = Modifier
                .size(32.dp),
            onClick = { onDownload(story) }
        ) {
            Icon(
                modifier = Modifier.size(ButtonDefaults.IconSize),
                imageVector = Icons.Outlined.FileDownload,
                contentDescription = "Download"
            )
        }

        IconButton(
            modifier = Modifier
                .size(32.dp),
            onClick = { onShare(story) }
        ) {
            Icon(
                modifier = Modifier.size(ButtonDefaults.IconSize),
                imageVector = Icons.Outlined.Share,
                contentDescription = "Share"
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier
                .defaultMinSize(minHeight = 1.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            onClick = { onPlay(story) }
        ) {
            Icon(
                modifier = Modifier.size(ButtonDefaults.IconSize),
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play"
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = story.humanReadablePlayTime,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@DevicePreviews
@Composable
private fun StoryItemPreview() {
    IgozogoTheme {
        StoryItem(
            story = PreviewStory,
            onItemClick = {}
        )
    }
}