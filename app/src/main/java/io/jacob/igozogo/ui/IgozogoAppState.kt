package io.jacob.igozogo.ui

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.util.trace
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import io.jacob.igozogo.feature.bookmark.navigation.navigateToBookmark
import io.jacob.igozogo.feature.home.navigation.HomeRoute
import io.jacob.igozogo.feature.home.navigation.navigateToHome
import io.jacob.igozogo.feature.search.navigation.navigateToSearch
import io.jacob.igozogo.feature.setting.navigation.navigateToSetting
import io.jacob.igozogo.navigation.BottomBarDestination
import timber.log.Timber

@Composable
fun rememberIgozogoAppState(
    navController: NavHostController = rememberNavController(),
    context: Context = LocalContext.current,
) = remember(navController, context) {
    IgozogoAppState(navController, context)
}

class IgozogoAppState(
    val navController: NavHostController,
    private val context: Context,
) {
    private val previousDestination = mutableStateOf<NavDestination?>(null)

    val currentDestination: NavDestination?
        @Composable get() {
            // Collect the currentBackStackEntryFlow as a state
            val currentEntry = navController.currentBackStackEntryFlow
                .collectAsState(initial = null)

            // Fallback to previousDestination if currentEntry is null
            return currentEntry.value?.destination.also { destination ->
                if (destination != null) {
                    previousDestination.value = destination
                }
            } ?: previousDestination.value
        }

    val currentBottomBarDestination: BottomBarDestination?
        @Composable get() {
            return BottomBarDestination.entries.firstOrNull { bottomBarDestination ->
                currentDestination?.hasRoute(route = bottomBarDestination.route) == true
            }
        }

    val bottomBarDestinations: List<BottomBarDestination> = BottomBarDestination.entries

    var isOnline by mutableStateOf(checkIfOnline())
        private set

    fun refreshOnline() {
        isOnline = checkIfOnline()
    }

    fun navigateToBottomBarDestination(destination: BottomBarDestination) {
        Timber.d("navigate to ${destination.name}, ${destination.baseRoute}")
        trace("Navigation: ${destination.name}") {
            val bottomBarNavOptions = navOptions {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
            val currentEntry = navController.currentBackStackEntry
            val currentDestination = currentEntry?.destination
            val startDestination = navController.graph.findStartDestination()
            Timber.d("startDestination: $startDestination")
            Timber.d("currentDestination: $currentDestination")
            currentEntry?.destination?.hierarchy?.forEachIndexed { index, h ->
                Timber.i("[$index] hierarchy: ${h.route}")
            }
            val isInHomeGraph = currentEntry?.destination
                ?.hierarchy
                ?.any { it.hasRoute(destination.baseRoute) } == true

            val isAtHomeRoot = currentDestination == startDestination

            Timber.i("isInHomeGraph: $isInHomeGraph")
            Timber.i("isAtHomeRoot: $isAtHomeRoot")
            when (destination) {
                BottomBarDestination.HOME -> {

                    when {
                        isInHomeGraph && !isAtHomeRoot -> {
                            navController.popBackStack(HomeRoute, inclusive = false)
                        }

                        !isInHomeGraph -> {
                            navController.navigateToHome(bottomBarNavOptions)
                        }

                        // 이미 HomeRoute이면 아무 동작 안 함
                    }
                }

                BottomBarDestination.SEARCH -> navController.navigateToSearch(bottomBarNavOptions)
                BottomBarDestination.BOOKMARK -> navController.navigateToBookmark(
                    bottomBarNavOptions
                )

                BottomBarDestination.SETTING -> navController.navigateToSetting(bottomBarNavOptions)
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    @Suppress("DEPRECATION")
    private fun checkIfOnline(): Boolean {
        val cm = getSystemService(context, ConnectivityManager::class.java)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities = cm?.getNetworkCapabilities(cm.activeNetwork) ?: return false
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } else {
            cm?.activeNetworkInfo?.isConnectedOrConnecting == true
        }
    }
}
