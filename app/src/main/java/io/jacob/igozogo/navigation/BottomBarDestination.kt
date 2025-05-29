package io.jacob.igozogo.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlin.reflect.KClass

enum class BottomBarDestination(
    @StringRes val label: Int,
    @DrawableRes val icon: Int,
    val route: KClass<*>,

) {
}