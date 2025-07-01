package io.jacob.igozogo.feature.storydetail.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.core.domain.model.Story
import io.jacob.igozogo.feature.storydetail.StoryDetailRoute
import io.jacob.igozogo.feature.storydetail.StoryDetailViewModel
import kotlinx.serialization.Serializable

@Serializable
data class StoryDetailRoute(val storyId: Int, val storyLangId: Int)

fun NavController.navigateToStoryDetail(
    story: Story, navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(route = StoryDetailRoute(story.storyId, story.storyLangId), navOptions)

fun NavGraphBuilder.storyDetailScreen(
    onPlaceClick: (Place) -> Unit,
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
) {
    composable<StoryDetailRoute> { entry ->
        val storyId = entry.toRoute<StoryDetailRoute>().storyId
        val storyLangId = entry.toRoute<StoryDetailRoute>().storyLangId
        StoryDetailRoute(
            viewModel = hiltViewModel<StoryDetailViewModel, StoryDetailViewModel.Factory>(
                key = "${storyId}:${storyLangId}"
            ) { factory ->
                factory.create(storyId, storyLangId)
            },
            onPlaceClick = onPlaceClick,
            onBackClick = onBackClick,
            onShowSnackbar = onShowSnackbar,
        )
    }
}