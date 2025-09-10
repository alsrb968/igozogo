package io.jacob.igozogo.ui

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import io.jacob.igozogo.feature.bookmark.navigation.navigateToBookmark
import io.jacob.igozogo.feature.home.navigation.navigateToHome
import io.jacob.igozogo.feature.search.navigation.navigateToSearch
import io.jacob.igozogo.feature.setting.navigation.navigateToSetting
import io.jacob.igozogo.navigation.BottomBarDestination
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

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

    val currentDestination: Flow<NavDestination?> =
        navController.currentBackStackEntryFlow.map { entry ->
            entry.destination.also { destination ->
                previousDestination.value = destination
            }
        }

    val currentBottomBarDestination: BottomBarDestination?
        @Composable get() {
            val current by currentDestination.collectAsStateWithLifecycle(initialValue = null)
            return BottomBarDestination.entries.firstOrNull { bottomBarDestination ->
                current?.hasRoute(route = bottomBarDestination.baseRoute) == true
            }
        }

    val bottomBarDestinations: List<BottomBarDestination> = BottomBarDestination.entries
    val startDestination = bottomBarDestinations.first()

    private val nestedNavControllers = mutableMapOf<BottomBarDestination, NavHostController>()

    fun registerNestedNavController(
        destination: BottomBarDestination,
        navController: NavHostController
    ) {
        nestedNavControllers[destination] = navController
    }

    private fun navigateToNestedNavRoot(destination: BottomBarDestination) {
        nestedNavControllers[destination]?.popBackStack(
            route = bottomBarDestinations.find { it == destination }?.route ?: return,
            inclusive = false
        )
    }

    suspend fun navigateToBottomBarDestination(destination: BottomBarDestination) {
        // 같은 탭을 다시 클릭한 경우 해당 탭의 root로 이동
        if (currentDestination.firstOrNull()?.hasRoute(destination.baseRoute) == true) {
            navigateToNestedNavRoot(destination)
            return
        }

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

    var isOnline by mutableStateOf(checkIfOnline())
        private set

    fun refreshOnline() {
        isOnline = checkIfOnline()
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
