package io.jacob.igozogo.feature.setting.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import io.jacob.igozogo.feature.setting.SettingRoute
import kotlinx.serialization.Serializable

@Serializable data object SettingRoute

fun NavController.navigateToSetting(navOptions: NavOptions) =
    navigate(route = SettingRoute, navOptions)

fun NavGraphBuilder.settingScreen(
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    composable<SettingRoute> {
        SettingRoute(
            onShowSnackbar = onShowSnackbar
        )
    }
}