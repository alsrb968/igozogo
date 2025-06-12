package io.jacob.igozogo.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import io.jacob.igozogo.R
import io.jacob.igozogo.feature.bookmark.navigation.BookmarkRoute
import io.jacob.igozogo.feature.home.navigation.HomeBaseRoute
import io.jacob.igozogo.feature.home.navigation.HomeRoute
import io.jacob.igozogo.feature.search.navigation.SearchRoute
import io.jacob.igozogo.feature.setting.navigation.SettingRoute
import kotlin.reflect.KClass

enum class BottomBarDestination(
    @StringRes val label: Int,
    @DrawableRes val icon: Int,
    val route: KClass<*>,
    val baseRoute: KClass<*> = route,
) {
    HOME(
        label = R.string.nav_home,
        icon = R.drawable.ic_home,
        route = HomeRoute::class,
        baseRoute = HomeBaseRoute::class,
    ),
    SEARCH(
        label = R.string.nav_search,
        icon = R.drawable.ic_search,
        route = SearchRoute::class,
    ),
    BOOKMARK(
        label = R.string.nav_bookmark,
        icon = R.drawable.ic_bookmark,
        route = BookmarkRoute::class,
    ),
    SETTING(
        label = R.string.nav_setting,
        icon = R.drawable.ic_setting,
        route = SettingRoute::class,
    ),
}