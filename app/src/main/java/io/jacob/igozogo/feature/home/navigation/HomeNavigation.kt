package io.jacob.igozogo.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import io.jacob.igozogo.feature.home.HomeRoute
import kotlinx.serialization.Serializable

@Serializable object HomeRoute

fun NavController.navigateToHome(navOptions: NavOptions) =
    navigate(route = HomeRoute, navOptions)

fun NavGraphBuilder.homeScreen(
    onSnackbar: (String) -> Unit,
) {
    composable<HomeRoute> {
        HomeRoute(
            onSnackbar = onSnackbar
        )
    }
}