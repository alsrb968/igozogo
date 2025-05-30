package io.jacob.igozogo.ui.shared

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.jacob.igozogo.ui.theme.IgozogoTheme
import io.jacob.igozogo.ui.tooling.DevicePreviews

@Composable
fun RowScope.IgozogoNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    alwaysShowLabel: Boolean = true,
    icon: @Composable () -> Unit,
    selectedIcon : @Composable () -> Unit = icon,
    label: @Composable (() -> Unit)? = null,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        modifier = modifier,
        enabled = enabled,
        label = label,
        alwaysShowLabel = alwaysShowLabel,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = IgozogoNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = IgozogoNavigationDefaults.navigationContentColor(),
            selectedTextColor = IgozogoNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = IgozogoNavigationDefaults.navigationContentColor(),
            indicatorColor = IgozogoNavigationDefaults.navigationIndicatorColor(),
        ),
    )
}

@Composable
fun IgozogoNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    NavigationBar(
        modifier = modifier,
        contentColor = IgozogoNavigationDefaults.navigationContentColor(),
        tonalElevation = 0.dp,
        content = content
    )
}

@DevicePreviews
@Composable
fun IgozogoNavigationBarPreview() {
    val items = listOf("Home", "Search", "Bookmark", "Setting")
    val icons = listOf(
        Icons.Outlined.Home,
        Icons.Outlined.Search,
        Icons.Outlined.Bookmarks,
        Icons.Outlined.Settings,
    )
    val selectedIcons = listOf(
        Icons.Filled.Home,
        Icons.Filled.Search,
        Icons.Filled.Bookmarks,
        Icons.Filled.Settings,
    )

    IgozogoTheme {
        IgozogoNavigationBar {
            items.forEachIndexed { index, item ->
                IgozogoNavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = icons[index],
                            contentDescription = null
                        )
                    },
                    selectedIcon = {
                        Icon(
                            imageVector = selectedIcons[index],
                            contentDescription = null
                        )
                    },
                    label = { Text(item) },
                    selected = index == 0,
                    onClick = { },
                )
            }
        }
    }
}

object IgozogoNavigationDefaults {
    @Composable
    fun navigationContentColor() = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)

    @Composable
    fun navigationSelectedItemColor() = MaterialTheme.colorScheme.onPrimaryContainer

    @Composable
    fun navigationIndicatorColor() = MaterialTheme.colorScheme.primaryContainer
}