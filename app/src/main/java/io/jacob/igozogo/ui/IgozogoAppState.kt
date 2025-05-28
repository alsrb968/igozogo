package io.jacob.igozogo.ui

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.filled.FindInPage
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FindInPage
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.jacob.igozogo.R

sealed class Screen(
    @StringRes val label: Int,
    val icons: Pair<ImageVector, ImageVector>,
    val route: String
) {
    data object Home : Screen(
        label = R.string.nav_home,
        icons = Icons.Filled.Home to Icons.Outlined.Home,
        route = ROUTE_HOME,
    )

    data object Search : Screen(
        label = R.string.nav_search,
        icons = Icons.Filled.FindInPage to Icons.Outlined.FindInPage,
        route = ROUTE_SEARCH,
    )

    data object Library : Screen(
        label = R.string.nav_library,
        icons = Icons.AutoMirrored.Filled.LibraryBooks to Icons.Outlined.LibraryBooks,
        route = ROUTE_LIBRARY,
    )

    data object Setting : Screen(
        label = R.string.nav_setting,
        icons = Icons.Filled.Settings to Icons.Outlined.Settings,
        route = ROUTE_SETTING,
    )

    companion object {
        const val ROUTE_HOME = "home"
        const val ROUTE_SEARCH = "search"
        const val ROUTE_LIBRARY = "library"
        const val ROUTE_SETTING = "setting"

        val screens = listOf(Home, Search, Library, Setting)
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
    var isOnline by mutableStateOf(checkIfOnline())
        private set

    fun refreshOnline() {
        isOnline = checkIfOnline()
    }

    fun navigateToBottomBarRoute(route: String) {
        if (route != navController.currentDestination?.route) {
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
                popUpTo(findStartDestination(navController.graph).id) {
                    saveState = true
                }
            }
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