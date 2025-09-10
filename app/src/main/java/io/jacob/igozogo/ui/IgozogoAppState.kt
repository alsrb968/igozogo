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
import io.jacob.igozogo.feature.bookmark.navigation.navigateToBookmark
import io.jacob.igozogo.feature.home.navigation.navigateToHome
import io.jacob.igozogo.feature.search.navigation.navigateToSearch
import io.jacob.igozogo.feature.setting.navigation.navigateToSetting
import io.jacob.igozogo.navigation.BottomBarDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

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
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val previousDestination = mutableStateOf<NavDestination?>(null)

    val currentDestination: StateFlow<NavDestination?> =
        navController.currentBackStackEntryFlow.map { entry ->
            entry.destination.also { destination ->
                previousDestination.value = destination
            }
        }.stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    val currentBottomBarDestination: BottomBarDestination?
        @Composable get() {
            return BottomBarDestination.entries.firstOrNull { bottomBarDestination ->
                currentDestination.value?.hasRoute(route = bottomBarDestination.baseRoute) == true
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
            route = destination.route,
            inclusive = false
        )
    }

    fun navigateToBottomBarDestination(destination: BottomBarDestination) {
        // 같은 탭을 다시 클릭한 경우 해당 탭의 root로 이동
        if (currentDestination.value?.hasRoute(destination.baseRoute) == true) {
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
