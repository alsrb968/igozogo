package io.jacob.igozogo.feature.bookmark.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.jacob.igozogo.feature.bookmark.BookmarkRoute
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Serializable data object BookmarkRoute

fun NavController.navigateToBookmark(navOptions: NavOptions) =
    navigate(route = BookmarkRoute, navOptions)

@Composable
private fun BookmarkNavHost(
    navController: NavHostController,
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
) {
    NavHost(
        navController = navController,
        startDestination = BookmarkRoute
    ) {
        composable<BookmarkRoute> {
            BookmarkRoute(
                onShowSnackbar = onShowSnackbar
            )
        }
    }
}

fun NavGraphBuilder.bookmarkSection(
    onRegisterNestedNavController: (KClass<*>, NavHostController) -> Unit,
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
) {
    composable<BookmarkRoute> {
        val navController = rememberNavController()

        LaunchedEffect(navController) {
            onRegisterNestedNavController(BookmarkRoute::class, navController)
        }

        BookmarkNavHost(
            navController = navController,
            onShowSnackbar = onShowSnackbar
        )
    }
}