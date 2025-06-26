package io.jacob.igozogo.feature.storydetail

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.jacob.igozogo.core.design.component.LoadingWheel
import io.jacob.igozogo.core.design.component.StateImage
import io.jacob.igozogo.core.design.theme.IgozogoTheme
import io.jacob.igozogo.core.design.tooling.DevicePreviews
import io.jacob.igozogo.core.design.tooling.PreviewStory
import io.jacob.igozogo.core.domain.model.Story

@Composable
fun StoryDetailRoute(
    modifier: Modifier = Modifier,
    viewModel: StoryDetailViewModel = hiltViewModel(),
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
                story = s.story,
                onBackClick = onBackClick,
                onShowSnackbar = onShowSnackbar
            )
        }
    }
}

@Composable
fun StoryDetailScreen(
    modifier: Modifier = Modifier,
    story: Story,
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
    ) {
        item {
            StateImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4 / 3f),
                imageUrl = story.imageUrl,
                contentDescription = story.title,
            )

            Text(
                modifier = Modifier
                    .padding(vertical = 16.dp),
                text = story.audioTitle,
                style = MaterialTheme.typography.headlineMedium,
            )

            Text(
                modifier = Modifier
                    .padding(vertical = 16.dp),
                text = story.title,
                style = MaterialTheme.typography.titleMedium,
            )

            Text(
                modifier = Modifier
                    .padding(vertical = 16.dp),
                text = story.script,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@DevicePreviews
@Composable
private fun StoryDetailScreenPreview() {
    IgozogoTheme {
        StoryDetailScreen(
            story = PreviewStory,
            onBackClick = {},
            onShowSnackbar = { _, _ -> true }
        )
    }
}