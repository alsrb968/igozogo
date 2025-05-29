package io.jacob.igozogo.feature.main

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
import androidx.navigation.compose.currentBackStackEntryAsState
import io.jacob.igozogo.navigation.BottomBarDestination
import io.jacob.igozogo.navigation.IgozogoNavHost
import io.jacob.igozogo.ui.IgozogoAppState
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
                tabs = appState.bottomBarDestinations,
                currentDestination = appState.currentBottomBarDestination ?: BottomBarDestination.HOME,
                navigateToDestination = appState::navigateToBottomBarDestination
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
    ) { contentPaddingValues ->
        IgozogoNavHost(
            modifier = Modifier
                .padding(contentPaddingValues),
            appState = appState,
            onSnackbar = ::onSnackbar
        )

//        NavHost(
//            modifier = modifier
//                .padding(contentPaddingValues),
//            navController = appState.navController,
//            startDestination = Screen.Home.route
//        ) {
//            composable(Screen.Home.route) { backstackEntry ->
//                HomeScreen(
//                    onSnackbar = ::onSnackbar
//                )
//            }
//
//            composable(Screen.Search.route) { backstackEntry ->
//
//            }
//
//            composable(Screen.Bookmark.route) { backstackEntry ->
//                BookmarkScreen(
//                    onSnackbar = ::onSnackbar
//                )
//            }
//
//            bookmarkScreen(
//                onSnackbar = ::onSnackbar
//            )
//
//            composable(Screen.Setting.route) { backstackEntry ->
//
//            }
//        }
    }
}

@Composable
fun IgozogoBottomBar(
    modifier: Modifier = Modifier,
    tabs: List<BottomBarDestination>,
    currentDestination: BottomBarDestination,
    navigateToDestination: (BottomBarDestination) -> Unit,
) {
    val currentSection = tabs.firstOrNull { it == currentDestination } ?: tabs.first()

    AnimatedVisibility(
        visible = tabs.map { it }.contains(currentDestination)
    ) {
        NavigationBar(
            modifier = modifier,
        ) {
            tabs.forEach { destination ->
                val selected = destination == currentSection
                val text = stringResource(destination.label)

                NavigationBarItem(
                    icon = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Icon(
                                modifier = Modifier,
                                painter = painterResource(id = destination.icon),
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
                    onClick = { navigateToDestination(destination) },
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
            tabs = BottomBarDestination.entries,
            currentDestination = BottomBarDestination.HOME,
            navigateToDestination = {}
        )
    }
}
