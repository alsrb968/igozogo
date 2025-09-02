package io.jacob.igozogo.feature.bookmark.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.jacob.igozogo.feature.bookmark.BookmarkRoute
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Serializable data object BookmarkRoute

fun NavController.navigateToBookmark(navOptions: NavOptions) =
    navigate(route = BookmarkRoute, navOptions)

@Composable
fun BookmarkNavHost(
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
    getNestedNavController: @Composable (KClass<*>) -> NavHostController,
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
) {
    composable<BookmarkRoute> {
        val navController = getNestedNavController(BookmarkRoute::class)
        BookmarkNavHost(
            navController = navController,
            onShowSnackbar = onShowSnackbar
        )
    }
}