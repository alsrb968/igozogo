package io.jacob.igozogo.ui.main.bookmark.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import io.jacob.igozogo.ui.main.bookmark.BookmarkRoute
import kotlinx.serialization.Serializable

@Serializable object BookmarkRoute

fun NavController.navigateToBookmark(navOptions: NavOptions) =
    navigate(route = BookmarkRoute, navOptions)

fun NavGraphBuilder.bookmarkScreen(
    onSnackbar: (String) -> Unit,
) {
    composable<BookmarkRoute> {
        BookmarkRoute(
            onSnackbar = onSnackbar
        )
    }
}