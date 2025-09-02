package io.jacob.igozogo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.jacob.igozogo.feature.bookmark.navigation.BookmarkRoute
import io.jacob.igozogo.feature.bookmark.navigation.bookmarkScreen
import io.jacob.igozogo.feature.home.navigation.HomeBaseRoute
import io.jacob.igozogo.feature.home.navigation.homeSection
import io.jacob.igozogo.feature.placedetail.navigation.navigateToPlaceDetail
import io.jacob.igozogo.feature.placedetail.navigation.placeDetailScreen
import io.jacob.igozogo.feature.search.navigation.searchSection
import io.jacob.igozogo.feature.setting.navigation.SettingRoute
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
    NavHost(
        navController = appState.navController,
        startDestination = HomeBaseRoute,
        modifier = modifier,
    ) {
        homeSection(
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
            navigateToPlaceDetail = { navigateToPlaceDetail(it) },
            navigateToStoryDetail = { navigateToStoryDetail(it) },
            onShowSnackbar = onShowSnackbar
        ) { nestedNavController ->
            addDetailsGraph(
                navController = nestedNavController,
                onShowSnackbar = onShowSnackbar
            )
        }
        
        composable<BookmarkRoute> {
            BookmarkTabNavHost(
                onShowSnackbar = onShowSnackbar
            )
        }
        
        composable<SettingRoute> {
            SettingTabNavHost(
                onShowSnackbar = onShowSnackbar
            )
        }
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

@Composable
private fun BookmarkTabNavHost(
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
) {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = BookmarkRoute
    ) {
        bookmarkScreen(
            onShowSnackbar = onShowSnackbar
        )
    }
}

@Composable
private fun SettingTabNavHost(
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
) {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = SettingRoute
    ) {
        settingScreen(
            onShowSnackbar = onShowSnackbar
        )
    }
}