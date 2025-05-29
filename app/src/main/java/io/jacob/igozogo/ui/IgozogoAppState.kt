package io.jacob.igozogo.ui

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Lifecycle
import androidx.navigation.*
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import io.jacob.igozogo.R
import io.jacob.igozogo.feature.bookmark.navigation.navigateToBookmark
import io.jacob.igozogo.feature.home.navigation.navigateToHome
import io.jacob.igozogo.navigation.BottomBarDestination

sealed class Screen(
    @StringRes val label: Int,
    @DrawableRes val icon: Int,
    val route: String
) {
    data object Home : Screen(
        label = R.string.nav_home,
        icon = R.drawable.ic_home,
        route = ROUTE_HOME,
    )

    data object Search : Screen(
        label = R.string.nav_search,
        icon = R.drawable.ic_search,
        route = ROUTE_SEARCH,
    )

    data object Bookmark : Screen(
        label = R.string.nav_bookmark,
        icon = R.drawable.ic_bookmark,
        route = ROUTE_BOOKMARK,
    )

    data object Setting : Screen(
        label = R.string.nav_setting,
        icon = R.drawable.ic_setting,
        route = ROUTE_SETTING,
    )

    companion object {
        const val ROUTE_HOME = "home"
        const val ROUTE_SEARCH = "search"
        const val ROUTE_BOOKMARK = "bookmark"
        const val ROUTE_SETTING = "setting"

        val screens = listOf(Home, Search, Bookmark, Setting)
    }
}

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
        val bottomBarNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (destination) {
            BottomBarDestination.HOME -> navController.navigateToHome(bottomBarNavOptions)
            BottomBarDestination.SEARCH -> navController.navigateToHome(bottomBarNavOptions)
            BottomBarDestination.BOOKMARK -> navController.navigateToBookmark(bottomBarNavOptions)
            BottomBarDestination.SETTING -> navController.navigateToHome(bottomBarNavOptions)

        }
    }

    fun navigateBack() {
        navController.popBackStack()
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

private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)

private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
}