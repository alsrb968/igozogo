package io.jacob.igozogo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import io.jacob.igozogo.feature.bookmark.navigation.bookmarkScreen
import io.jacob.igozogo.feature.home.navigation.HomeBaseRoute
import io.jacob.igozogo.feature.home.navigation.homeSection
import io.jacob.igozogo.feature.placedetail.navigation.navigateToPlaceDetail
import io.jacob.igozogo.feature.placedetail.navigation.placeDetailScreen
import io.jacob.igozogo.feature.search.navigation.searchSection
import io.jacob.igozogo.feature.setting.navigation.settingScreen
import io.jacob.igozogo.feature.storydetail.navigation.navigateToStoryDetail
import io.jacob.igozogo.feature.storydetail.navigation.storyDetailScreen
import io.jacob.igozogo.ui.IgozogoAppState

@Composable
fun IgozogoNavHost(
    modifier: Modifier = Modifier,
    appState: IgozogoAppState,
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = HomeBaseRoute,
        modifier = modifier,
    ) {
        homeSection(
            onPlaceClick = navController::navigateToPlaceDetail,
            onStoryClick = navController::navigateToStoryDetail,
            onShowSnackbar = onShowSnackbar
        ) {
            placeDetailScreen(
                onStoryClick = navController::navigateToStoryDetail,
                onBackClick = navController::popBackStack,
                onShowSnackbar = onShowSnackbar
            )
            storyDetailScreen(
                onPlaceClick = navController::navigateToPlaceDetail,
                onBackClick = navController::popBackStack,
                onShowSnackbar = onShowSnackbar
            )
        }
        searchSection(
            onPlaceClick = navController::navigateToPlaceDetail,
            onStoryClick = navController::navigateToStoryDetail,
            onShowSnackbar = onShowSnackbar
        ) {
            placeDetailScreen(
                onStoryClick = navController::navigateToStoryDetail,
                onBackClick = navController::popBackStack,
                onShowSnackbar = onShowSnackbar
            )
            storyDetailScreen(
                onPlaceClick = navController::navigateToPlaceDetail,
                onBackClick = navController::popBackStack,
                onShowSnackbar = onShowSnackbar
            )
        }
        bookmarkScreen(
            onShowSnackbar = onShowSnackbar
        )
        settingScreen(
            onShowSnackbar = onShowSnackbar
        )
    }
}