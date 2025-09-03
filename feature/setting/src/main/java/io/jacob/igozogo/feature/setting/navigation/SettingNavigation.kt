package io.jacob.igozogo.feature.setting.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.jacob.igozogo.feature.setting.SettingRoute
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Serializable data object SettingRoute

fun NavController.navigateToSetting(navOptions: NavOptions) =
    navigate(route = SettingRoute, navOptions)

@Composable
private fun SettingNavHost(
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
    onRegisterNestedNavController: (KClass<*>, NavHostController) -> Unit,
    onShowSnackbar: suspend (message: String, actionLabel: String?) -> Boolean,
) {
    composable<SettingRoute> {
        val navController = rememberNavController()

        LaunchedEffect(navController) {
            onRegisterNestedNavController(SettingRoute::class, navController)
        }

        SettingNavHost(
            navController = navController,
            onShowSnackbar = onShowSnackbar
        )
    }
}