package io.jacob.igozogo.ui

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import io.jacob.igozogo.feature.bookmark.navigation.BookmarkRoute
import io.jacob.igozogo.feature.bookmark.navigation.navigateToBookmark
import io.jacob.igozogo.feature.home.navigation.HomeRoute
import io.jacob.igozogo.feature.home.navigation.navigateToHome
import io.jacob.igozogo.feature.search.navigation.SearchRoute
import io.jacob.igozogo.feature.search.navigation.navigateToSearch
import io.jacob.igozogo.feature.setting.navigation.SettingRoute
import io.jacob.igozogo.feature.setting.navigation.navigateToSetting
import io.jacob.igozogo.navigation.BottomBarDestination
import kotlin.reflect.KClass

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
    private val nestedNavControllers = mutableMapOf<BottomBarDestination, NavHostController>()

    fun registerNestedNavController(
        destination: BottomBarDestination,
        route: KClass<*>,
        navController: NavHostController
    ) {
        nestedNavControllers[destination] = navController
    }

    private fun navigateToNestedNavRoot(destination: BottomBarDestination) {
        nestedNavControllers[destination]?.popBackStack(
            route = when (destination) {
                BottomBarDestination.HOME -> HomeRoute
                BottomBarDestination.SEARCH -> SearchRoute
                BottomBarDestination.BOOKMARK -> BookmarkRoute
                BottomBarDestination.SETTING -> SettingRoute
            },
            inclusive = false
        )
    }

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
                currentDestination?.hasRoute(route = bottomBarDestination.baseRoute) == true
            }
        }

    val bottomBarDestinations: List<BottomBarDestination> = BottomBarDestination.entries

    var isOnline by mutableStateOf(checkIfOnline())
        private set

    fun refreshOnline() {
        isOnline = checkIfOnline()
    }

    private var lastSelectedDestination by mutableStateOf<BottomBarDestination?>(null)

    fun navigateToBottomBarDestination(destination: BottomBarDestination) {
        // 같은 탭을 다시 클릭한 경우 해당 탭의 root로 이동
        if (lastSelectedDestination == destination) {
            navigateToNestedNavRoot(destination)
            return
        }

        lastSelectedDestination = destination

        val bottomBarNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (destination) {
            BottomBarDestination.HOME -> navController.navigateToHome(bottomBarNavOptions)
            BottomBarDestination.SEARCH -> navController.navigateToSearch(bottomBarNavOptions)
            BottomBarDestination.BOOKMARK -> navController.navigateToBookmark(bottomBarNavOptions)
            BottomBarDestination.SETTING -> navController.navigateToSetting(bottomBarNavOptions)
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
