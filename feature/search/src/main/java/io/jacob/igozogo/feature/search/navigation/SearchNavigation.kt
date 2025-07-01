package io.jacob.igozogo.feature.search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import io.jacob.igozogo.feature.search.SearchRoute
import kotlinx.serialization.Serializable

@Serializable data object SearchRoute

fun NavController.navigateToSearch(navOptions: NavOptions) =
    navigate(route = SearchRoute, navOptions)

fun NavGraphBuilder.searchScreen(
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
) {
    composable<SearchRoute> {
        SearchRoute(
            onShowSnackbar = onShowSnackbar
        )
    }
}