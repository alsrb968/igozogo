package io.jacob.igozogo.core.design.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
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
import io.jacob.igozogo.core.domain.util.toHumanReadableDate

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
                        .size(90.dp)
                        .clip(shape = RoundedCornerShape(8.dp)),
                    imageUrl = story.imageUrl,
                    contentDescription = story.audioTitle
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier
                ) {
                    Text(
                        text = story.modifiedTime.toHumanReadableDate(),
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
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = { /* TODO */ }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.PlaylistAdd,
                        contentDescription = "Add to playlist"
                    )
                }
            }
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