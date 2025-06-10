package io.jacob.igozogo.core.design.component

import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.jacob.igozogo.core.domain.model.Story

@Composable
fun StoryItem(
    modifier: Modifier = Modifier,
    story: Story,
    onItemClick: (Story) -> Unit
) {
    Card(
        modifier = modifier,
        onClick = { onItemClick(story) }
    ) {

    }
}