package io.jacob.igozogo.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import io.jacob.igozogo.core.design.icon.IgozogoIcons
import io.jacob.igozogo.feature.bookmark.navigation.BookmarkRoute
import io.jacob.igozogo.feature.home.navigation.HomeBaseRoute
import io.jacob.igozogo.feature.home.navigation.HomeRoute
import io.jacob.igozogo.feature.search.navigation.SearchBaseRoute
import io.jacob.igozogo.feature.search.navigation.SearchRoute
import io.jacob.igozogo.feature.setting.navigation.SettingRoute
import kotlin.reflect.KClass
import io.jacob.igozogo.core.design.R as designR

enum class BottomBarDestination(
    @StringRes val label: Int,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val route: KClass<*>,
    val baseRoute: KClass<*> = route,
) {
    HOME(
        label = designR.string.core_design_home,
        icon = IgozogoIcons.HomeBorder,
        selectedIcon = IgozogoIcons.Home,
        route = HomeRoute::class,
        baseRoute = HomeBaseRoute::class,
    ),
    SEARCH(
        label = designR.string.core_design_search,
        icon = IgozogoIcons.SearchBorder,
        selectedIcon = IgozogoIcons.Search,
        route = SearchRoute::class,
        baseRoute = SearchBaseRoute::class,
    ),
    BOOKMARK(
        label = designR.string.core_design_bookmark,
        icon = IgozogoIcons.BookmarksBorder,
        selectedIcon = IgozogoIcons.Bookmarks,
        route = BookmarkRoute::class,
    ),
    SETTING(
        label = designR.string.core_design_setting,
        icon = IgozogoIcons.SettingsBorder,
        selectedIcon = IgozogoIcons.Settings,
        route = SettingRoute::class,
    ),
}