package io.jacob.igozogo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import io.jacob.igozogo.feature.bookmark.navigation.bookmarkScreen
import io.jacob.igozogo.feature.home.navigation.HomeRoute
import io.jacob.igozogo.feature.home.navigation.homeScreen
import io.jacob.igozogo.ui.IgozogoAppState

@Composable
fun IgozogoNavHost(
    modifier: Modifier = Modifier,
    appState: IgozogoAppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        modifier = modifier,
    ) {
        homeScreen(
            onShowSnackbar = onShowSnackbar
        )
        bookmarkScreen(
            onShowSnackbar = onShowSnackbar
        )
    }
}