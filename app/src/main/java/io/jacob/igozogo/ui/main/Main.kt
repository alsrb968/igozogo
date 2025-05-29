package io.jacob.igozogo.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import io.jacob.igozogo.ui.IgozogoAppState
import io.jacob.igozogo.ui.Screen
import io.jacob.igozogo.ui.main.bookmark.BookmarkScreen
import io.jacob.igozogo.ui.main.bookmark.navigation.bookmarkScreen
import io.jacob.igozogo.ui.main.home.HomeScreen
import io.jacob.igozogo.ui.theme.IgozogoTheme
import io.jacob.igozogo.ui.tooling.DevicePreviews
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    appState: IgozogoAppState,
) {
    val scope = rememberCoroutineScope()
    val navBackStackEntry by appState.navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val snackbarHostState = remember { SnackbarHostState() }

    fun onSnackbar(message: String) {
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(message)
        }
    }
    Scaffold(
        modifier = modifier,
//            topBar = {
//                TopAppBar(
//                    title = {
//                        Text(
//                            text = stringResource(R.string.app_name),
//                            style = MaterialTheme.typography.displaySmall
//                        )
//                    },
//                    actions = {
//                        IconButton(onClick = { /*TODO*/ }) {
//                            Icon(
//                                imageVector = Icons.Default.MoreVert,
//                                contentDescription = "More"
//                            )
//                        }
//                    }
//                )
//            },
        bottomBar = {
            IgozogoBottomBar(
                tabs = Screen.screens,
                currentRoute = currentRoute ?: Screen.Home.route,
                navigateToRoute = appState::navigateToBottomBarRoute
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
    ) { contentPaddingValues ->
        NavHost(
            modifier = modifier
                .padding(contentPaddingValues),
            navController = appState.navController,
            startDestination = Screen.Home.route
        ) {
            composable(Screen.Home.route) { backstackEntry ->
                HomeScreen(
                    onSnackbar = ::onSnackbar
                )
            }

            composable(Screen.Search.route) { backstackEntry ->

            }

            composable(Screen.Bookmark.route) { backstackEntry ->
                BookmarkScreen(
                    onSnackbar = ::onSnackbar
                )
            }

            bookmarkScreen(
                onSnackbar = ::onSnackbar
            )

            composable(Screen.Setting.route) { backstackEntry ->

            }
        }
    }
}

@Composable
fun IgozogoBottomBar(
    modifier: Modifier = Modifier,
    tabs: List<Screen>,
    currentRoute: String,
    navigateToRoute: (String) -> Unit,
) {
    val currentSection = tabs.firstOrNull { it.route == currentRoute } ?: tabs.first()

    AnimatedVisibility(
        visible = tabs.map { it.route }.contains(currentRoute)
    ) {
        NavigationBar(
            modifier = modifier,
        ) {
            tabs.forEach { screen ->
                val selected = screen == currentSection
                val text = stringResource(screen.label)

                NavigationBarItem(
                    icon = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Icon(
                                modifier = Modifier,
                                painter = painterResource(id = screen.icon),
                                contentDescription = text
                            )

                            Text(
                                text = text,
                                style = MaterialTheme.typography.labelLarge,
                                maxLines = 1
                            )
                        }

                    },
                    selected = selected,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        indicatorColor = Color.Transparent
                    ),
                    onClick = { navigateToRoute(screen.route) },
                )
            }
        }
    }
}

@DevicePreviews
@Composable
private fun IgozogoBottomBarPreview() {
    IgozogoTheme {
        IgozogoBottomBar(
            tabs = Screen.screens,
            currentRoute = Screen.Home.route,
            navigateToRoute = {}
        )
    }
}
