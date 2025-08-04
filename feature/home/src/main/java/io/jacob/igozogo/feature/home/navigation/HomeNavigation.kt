package io.jacob.igozogo.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import io.jacob.igozogo.core.model.Place
import io.jacob.igozogo.core.model.Story
import io.jacob.igozogo.feature.home.HomeRoute
import kotlinx.serialization.Serializable

@Serializable data object HomeRoute

@Serializable data object HomeBaseRoute

fun NavController.navigateToHome(navOptions: NavOptions) =
    navigate(route = HomeRoute, navOptions)

fun NavGraphBuilder.homeSection(
    onPlaceClick: (Place) -> Unit,
    onStoryClick: (Story) -> Unit,
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
    destination: NavGraphBuilder.() -> Unit,
) {
    navigation<HomeBaseRoute>(startDestination = HomeRoute) {
        composable<HomeRoute> {
            HomeRoute(
                onPlaceClick = onPlaceClick,
                onStoryClick = onStoryClick,
                onShowSnackbar = onShowSnackbar
            )
        }

        destination()
    }
}