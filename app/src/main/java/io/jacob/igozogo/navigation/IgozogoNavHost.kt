package io.jacob.igozogo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import io.jacob.igozogo.feature.bookmark.navigation.bookmarkSection
import io.jacob.igozogo.feature.home.navigation.HomeBaseRoute
import io.jacob.igozogo.feature.home.navigation.homeSection
import io.jacob.igozogo.feature.placedetail.navigation.navigateToPlaceDetail
import io.jacob.igozogo.feature.placedetail.navigation.placeDetailScreen
import io.jacob.igozogo.feature.search.navigation.searchSection
import io.jacob.igozogo.feature.setting.navigation.settingSection
import io.jacob.igozogo.feature.storydetail.navigation.navigateToStoryDetail
import io.jacob.igozogo.feature.storydetail.navigation.storyDetailScreen
import io.jacob.igozogo.ui.IgozogoAppState

@Composable
fun IgozogoNavHost(
    modifier: Modifier = Modifier,
    appState: IgozogoAppState,
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
) {
    NavHost(
        navController = appState.navController,
        startDestination = HomeBaseRoute,
        modifier = modifier,
    ) {
        homeSection(
            getNestedNavController = { appState.getNestedNavController(it) },
            navigateToPlaceDetail = { navigateToPlaceDetail(it) },
            navigateToStoryDetail = { navigateToStoryDetail(it) },
            onShowSnackbar = onShowSnackbar
        ) { nestedNavController ->
            addDetailsGraph(
                navController = nestedNavController,
                onShowSnackbar = onShowSnackbar
            )
        }
        
        searchSection(
            getNestedNavController = { appState.getNestedNavController(it) },
            navigateToPlaceDetail = { navigateToPlaceDetail(it) },
            navigateToStoryDetail = { navigateToStoryDetail(it) },
            onShowSnackbar = onShowSnackbar
        ) { nestedNavController ->
            addDetailsGraph(
                navController = nestedNavController,
                onShowSnackbar = onShowSnackbar
            )
        }
        
        bookmarkSection(
            getNestedNavController = { appState.getNestedNavController(it) },
            onShowSnackbar = onShowSnackbar
        )
        
        settingSection(
            getNestedNavController = { appState.getNestedNavController(it) },
            onShowSnackbar = onShowSnackbar
        )
    }
}

fun NavGraphBuilder.addDetailsGraph(
    navController: NavController,
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
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

