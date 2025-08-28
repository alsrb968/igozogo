package io.jacob.igozogo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.jacob.igozogo.feature.bookmark.navigation.BookmarkRoute
import io.jacob.igozogo.feature.bookmark.navigation.bookmarkScreen
import io.jacob.igozogo.feature.home.navigation.HomeBaseRoute
import io.jacob.igozogo.feature.home.navigation.homeSection
import io.jacob.igozogo.feature.placedetail.navigation.navigateToPlaceDetail
import io.jacob.igozogo.feature.placedetail.navigation.placeDetailScreen
import io.jacob.igozogo.feature.search.navigation.SearchBaseRoute
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
    // 메인 하단 네비게이션바를 위한 NavHost
    NavHost(
        navController = appState.navController,
        startDestination = HomeBaseRoute,
        modifier = modifier,
    ) {
        // 각 탭은 독립적인 컴포넌트로 구성
        composable<HomeBaseRoute> {
            HomeTabNavHost(
                onShowSnackbar = onShowSnackbar
            )
        }
        
        composable<SearchBaseRoute> {
            SearchTabNavHost(
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

// 각 탭별 독립적인 NavHost들

@Composable
private fun HomeTabNavHost(
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
) {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = HomeBaseRoute
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
    }
}

@Composable
private fun SearchTabNavHost(
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
) {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = SearchBaseRoute
    ) {
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
    }
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