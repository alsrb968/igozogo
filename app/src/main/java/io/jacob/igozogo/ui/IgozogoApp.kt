package io.jacob.igozogo.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import io.jacob.igozogo.R
import io.jacob.igozogo.core.design.component.IgozogoNavigationBar
import io.jacob.igozogo.core.design.component.IgozogoNavigationBarItem
import io.jacob.igozogo.core.design.theme.IgozogoTheme
import io.jacob.igozogo.core.design.tooling.DevicePreviews
import io.jacob.igozogo.feature.player.PlayerMiniBar
import io.jacob.igozogo.navigation.IgozogoNavHost
import kotlin.reflect.KClass

@Composable
fun IgozogoApp(
    modifier: Modifier = Modifier,
    appState: IgozogoAppState = rememberIgozogoAppState()
) {
    if (appState.isOnline) {
        val snackbarHostState = remember { SnackbarHostState() }

        IgozogoApp(
            modifier = modifier,
            appState = appState,
            snackbarHostState = snackbarHostState,
        )
    } else {
        OfflineDialog { appState.refreshOnline() }
    }
}

@Composable
fun IgozogoApp(
    modifier: Modifier = Modifier,
    appState: IgozogoAppState,
    snackbarHostState: SnackbarHostState,
) {
    val currentDestination = appState.currentDestination

    Scaffold(
        modifier = modifier,
        bottomBar = {
            IgozogoNavigationBar {
                appState.bottomBarDestinations.forEach { destination ->
                    val selected = currentDestination
                        .isRouteInHierarchy(destination.baseRoute)
                    val text = stringResource(destination.label)

                    IgozogoNavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = destination.icon),
                                contentDescription = text
                            )
                        },
                        label = {
                            Text(
                                text = text,
                                style = MaterialTheme.typography.labelLarge,
                                maxLines = 1
                            )
                        },
                        selected = selected,
                        onClick = { appState.navigateToBottomBarDestination(destination) },
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(
                snackbarHostState,
                modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing),
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            IgozogoNavHost(
                appState = appState,
                onShowSnackbar = { message, action ->
                    snackbarHostState.showSnackbar(
                        message = message,
                        actionLabel = action,
                        duration = SnackbarDuration.Short,
                    ) == SnackbarResult.ActionPerformed
                },
            )

            PlayerMiniBar()
        }
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

private fun NavDestination?.isRouteInHierarchy(route: KClass<*>) =
    this?.hierarchy?.any {
        it.hasRoute(route)
    } ?: false

@DevicePreviews
@Composable
private fun OfflineDialogPreview() {
    IgozogoTheme {
        OfflineDialog(onRetry = {})
    }
}