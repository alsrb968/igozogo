package io.jacob.igozogo.feature.bookmark.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import io.jacob.igozogo.feature.bookmark.BookmarkRoute
import kotlinx.serialization.Serializable

@Serializable data object BookmarkRoute

fun NavController.navigateToBookmark(navOptions: NavOptions) =
    navigate(route = BookmarkRoute, navOptions)

fun NavGraphBuilder.bookmarkScreen(
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    composable<BookmarkRoute> {
        BookmarkRoute(
            onShowSnackbar = onShowSnackbar
        )
    }
}