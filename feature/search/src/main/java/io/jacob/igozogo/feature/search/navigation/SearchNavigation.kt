package io.jacob.igozogo.feature.search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import io.jacob.igozogo.core.model.Place
import io.jacob.igozogo.core.model.Story
import io.jacob.igozogo.feature.search.SearchRoute
import kotlinx.serialization.Serializable

@Serializable data object SearchRoute

@Serializable data object SearchBaseRoute

fun NavController.navigateToSearch(navOptions: NavOptions) =
    navigate(route = SearchRoute, navOptions)

fun NavGraphBuilder.searchSection(
    onPlaceClick: (Place) -> Unit,
    onStoryClick: (Story) -> Unit,
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
    destination: NavGraphBuilder.() -> Unit,
) {
    navigation<SearchBaseRoute>(startDestination = SearchRoute) {
        composable<SearchRoute> {
            SearchRoute(
                onPlaceClick = onPlaceClick,
                onStoryClick = onStoryClick,
                onShowSnackbar = onShowSnackbar
            )
        }
    }

    destination()
}