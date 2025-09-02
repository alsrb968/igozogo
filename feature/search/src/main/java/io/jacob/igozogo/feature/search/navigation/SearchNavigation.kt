package io.jacob.igozogo.feature.search.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.jacob.igozogo.core.model.Place
import io.jacob.igozogo.core.model.Story
import io.jacob.igozogo.feature.search.SearchRoute
import kotlinx.serialization.Serializable

@Serializable data object SearchRoute

@Serializable data object SearchBaseRoute

fun NavController.navigateToSearch(navOptions: NavOptions) =
    navigate(route = SearchBaseRoute, navOptions)

private fun NavGraphBuilder.searchScreen(
    onPlaceClick: (Place) -> Unit,
    onStoryClick: (Story) -> Unit,
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
) {
    composable<SearchRoute> {
        SearchRoute(
            onPlaceClick = onPlaceClick,
            onStoryClick = onStoryClick,
            onShowSnackbar = onShowSnackbar
        )
    }
}

@Composable
private fun SearchNavHost(
    navigateToPlaceDetail: NavController.(Place) -> Unit,
    navigateToStoryDetail: NavController.(Story) -> Unit,
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
    destination: NavGraphBuilder.(NavController) -> Unit,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SearchRoute
    ) {
        searchScreen(
            onPlaceClick = { navController.navigateToPlaceDetail(it) },
            onStoryClick = { navController.navigateToStoryDetail(it) },
            onShowSnackbar = onShowSnackbar,
        )

        destination(navController)
    }
}

fun NavGraphBuilder.searchSection(
    navigateToPlaceDetail: NavController.(Place) -> Unit,
    navigateToStoryDetail: NavController.(Story) -> Unit,
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
    destination: NavGraphBuilder.(NavController) -> Unit,
) {
    composable<SearchBaseRoute> {
        SearchNavHost(
            navigateToPlaceDetail = navigateToPlaceDetail,
            navigateToStoryDetail = navigateToStoryDetail,
            onShowSnackbar = onShowSnackbar,
            destination = destination
        )
    }
}