package io.jacob.igozogo.feature.setting.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.jacob.igozogo.feature.setting.SettingRoute
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Serializable data object SettingRoute

fun NavController.navigateToSetting(navOptions: NavOptions) =
    navigate(route = SettingRoute, navOptions)

@Composable
fun SettingNavHost(
    navController: NavHostController,
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
) {
    NavHost(
        navController = navController,
        startDestination = SettingRoute
    ) {
        composable<SettingRoute> {
            SettingRoute(
                onShowSnackbar = onShowSnackbar
            )
        }
    }
}

fun NavGraphBuilder.settingSection(
    getNestedNavController: @Composable (KClass<*>) -> NavHostController,
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
) {
    composable<SettingRoute> {
        val navController = getNestedNavController(SettingRoute::class)
        SettingNavHost(
            navController = navController,
            onShowSnackbar = onShowSnackbar
        )
    }
}