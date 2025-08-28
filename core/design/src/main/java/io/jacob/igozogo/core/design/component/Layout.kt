@file:OptIn(ExperimentalMaterial3Api::class)

package io.jacob.igozogo.core.design.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.jacob.igozogo.core.design.R
import io.jacob.igozogo.core.design.icon.IgozogoIcons
import io.jacob.igozogo.core.design.theme.IgozogoTheme
import io.jacob.igozogo.core.design.tooling.DevicePreviews

@Composable
fun SectionHeader(
    modifier: Modifier = Modifier,
    text: String,
    onMore: (() -> Unit)? = null,
    content: @Composable () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            onMore?.let {
                TextButton(
                    onClick = it,
                ) {
                    Text(text = stringResource(R.string.core_design_more))
                }
            }
        }

        content()
    }
}

@Composable
fun IgozogoScaffold(
    modifier: Modifier = Modifier,
    title: String,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    content: @Composable (PaddingValues, NestedScrollConnection) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            IgozogoTopAppBar(
                title = title,
                scrollBehavior = scrollBehavior
            )
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets
            .exclude(WindowInsets.navigationBars)
    ) { paddingValues ->
        content(
            paddingValues,
            scrollBehavior.nestedScrollConnection
        )
    }
}

@Composable
fun FadeTopBarLayout(
    modifier: Modifier = Modifier,
    state: LazyListState,
    offset: Int = 700,
    title: String,
    onBack: () -> Unit,
    content: @Composable () -> Unit
) {
    val showTopBar by remember {
        derivedStateOf {
            val firstVisibleItem = state.firstVisibleItemIndex > 0
            val offsetPastFirst = state.firstVisibleItemIndex == 0 &&
                    state.firstVisibleItemScrollOffset > offset
            firstVisibleItem || offsetPastFirst
        }
    }

    Box(modifier = modifier) {
        content()

        TopAppBar(
            modifier = Modifier,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = if (showTopBar) 1f else 0f)
            ),
            title = {
                AnimatedVisibility(
                    visible = showTopBar,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = title,
                    )
                }
            },
            navigationIcon = {
                IconButton(
                    onClick = onBack
                ) {
                    Icon(
                        imageVector = IgozogoIcons.Back,
                        contentDescription = "Back",
                    )
                }
            },
        )
    }
}

@DevicePreviews
@Composable
private fun SectionHeaderPreview() {
    IgozogoTheme {
        SectionHeader(
            text = "Preview",
            onMore = {}
        )
    }
}

@DevicePreviews
@Composable
private fun IgozogoScaffoldPreview() {
    IgozogoTheme {
        IgozogoScaffold(
            title = "Title",
        ) { paddingValues, _ ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        }
    }
}