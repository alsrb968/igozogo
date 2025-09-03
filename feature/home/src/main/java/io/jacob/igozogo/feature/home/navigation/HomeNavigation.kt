package io.jacob.igozogo.feature.home.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.jacob.igozogo.core.model.Place
import io.jacob.igozogo.core.model.Story
import io.jacob.igozogo.feature.home.HomeRoute
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Serializable data object HomeRoute

@Serializable data object HomeBaseRoute

fun NavController.navigateToHome(navOptions: NavOptions) =
    navigate(route = HomeBaseRoute, navOptions)

private fun NavGraphBuilder.homeScreen(
    onPlaceClick: (Place) -> Unit,
    onStoryClick: (Story) -> Unit,
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
) {
    composable<HomeRoute> {
        HomeRoute(
            onPlaceClick = onPlaceClick,
            onStoryClick = onStoryClick,
            onShowSnackbar = onShowSnackbar
        )
    }
}

@Composable
private fun HomeNavHost(
    navController: NavHostController,
    navigateToPlaceDetail: NavController.(Place) -> Unit,
    navigateToStoryDetail: NavController.(Story) -> Unit,
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
    destination: NavGraphBuilder.(NavController) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoute
    ) {
        homeScreen(
            onPlaceClick = { navController.navigateToPlaceDetail(it) },
            onStoryClick = { navController.navigateToStoryDetail(it) },
            onShowSnackbar = onShowSnackbar,
        )

        destination(navController)
    }
}

fun NavGraphBuilder.homeSection(
    onRegisterNestedNavController: (KClass<*>, NavHostController) -> Unit,
    navigateToPlaceDetail: NavController.(Place) -> Unit,
    navigateToStoryDetail: NavController.(Story) -> Unit,
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
    destination: NavGraphBuilder.(NavController) -> Unit,
) {
    composable<HomeBaseRoute> {
        val navController = rememberNavController()
        
        LaunchedEffect(navController) {
            onRegisterNestedNavController(HomeRoute::class, navController)
        }
        
        HomeNavHost(
            navController = navController,
            navigateToPlaceDetail = navigateToPlaceDetail,
            navigateToStoryDetail = navigateToStoryDetail,
            onShowSnackbar = onShowSnackbar,
            destination = destination
        )
    }
}