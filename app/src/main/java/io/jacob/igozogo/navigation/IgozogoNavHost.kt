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
    onSnackbar: (String) -> Unit,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        modifier = modifier,
    ) {
        homeScreen(
            onSnackbar = onSnackbar
        )
        bookmarkScreen(
            onSnackbar = onSnackbar
        )
    }
}