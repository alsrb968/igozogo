package io.jacob.igozogo.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.core.os.ConfigurationCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import io.jacob.igozogo.R
import io.jacob.igozogo.ui.home.HomeScreen
import io.jacob.igozogo.ui.theme.IgozogoTheme
import io.jacob.igozogo.ui.tooling.DevicePreviews
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IgozogoApp(
    modifier: Modifier = Modifier,
    appState: IgozogoAppState = rememberIgozogoAppState()
) {
    if (appState.isOnline) {
        val coroutineScope = rememberCoroutineScope()
        val navBackStackEntry by appState.navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val snackbarHostState = remember { SnackbarHostState() }

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

            },
        ) { contentPaddingValues ->
            NavHost(
                modifier = modifier
                    .padding(contentPaddingValues)
                    .background(MaterialTheme.colorScheme.background),
                navController = appState.navController,
                startDestination = Screen.Home.route
            ) {
                composable(Screen.Home.route) { backstackEntry ->
                    HomeScreen()
                }

                composable(Screen.Search.route) { backstackEntry ->

                }

                composable(Screen.Bookmark.route) { backstackEntry ->

                }

                composable(Screen.Setting.route) { backstackEntry ->

                }
            }
        }
    } else {
        OfflineDialog { appState.refreshOnline() }
    }
}

@Composable
fun IgozogoBottomBar(
    tabs: List<Screen>,
    currentRoute: String,
    navigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.background.copy(alpha = 0.3f),
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
) {
    val routes = remember { tabs.map { it.route } }
    val currentSection = tabs.first { it.route == currentRoute }

    AnimatedVisibility(
        visible = tabs.map { it.route }.contains(currentRoute)
    ) {
        NavigationBar(
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
//                .height(85.dp)
//                .gradientBackground(
//                    ratio = 0.7f,
//                    startColor = Color.Transparent,
//                    endColor = MaterialTheme.colorScheme.background
//                )
            ,
            containerColor = color,
            contentColor = contentColor,
        ) {
            val configuration = LocalConfiguration.current
            val currentLocale: Locale =
                ConfigurationCompat.getLocales(configuration).get(0) ?: Locale.getDefault()

            tabs.forEach { screen ->
                val selected = screen == currentSection
                val text = stringResource(screen.label)

                NavigationBarItem(
//                    modifier = Modifier
//                        .padding(horizontal = 16.dp),
                    icon = {
                        val iconSelected = screen.icons.second
                        val iconNormal = screen.icons.first
                        Icon(
                            imageVector = screen.icons.let { if (selected) iconNormal else iconSelected },
                            contentDescription = text
                        )
                    },
                    label = {
                        Text(
                            text = text,
//                            style = MaterialTheme.typography.labelLarge,
                            maxLines = 1
                        )
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

@Composable
fun OfflineDialog(onRetry: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = stringResource(R.string.network_error_title)) },
        text = { Text(text = stringResource(R.string.network_error_message)) },
        confirmButton = {
            TextButton(onClick = onRetry) {
                Text(text = stringResource(R.string.retry))
            }
        }
    )
}

@DevicePreviews
@Composable
private fun OfflineDialogPreview() {
    IgozogoTheme {
        OfflineDialog(onRetry = {})
    }
}