@file:OptIn(ExperimentalMaterial3Api::class)

package io.jacob.igozogo.core.design.component

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.jacob.igozogo.core.design.theme.IgozogoTheme
import io.jacob.igozogo.core.design.tooling.DevicePreviews
import io.jacob.igozogo.core.design.R as designR

@Composable
fun IgozogoTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
            )
        },
        scrollBehavior = scrollBehavior
    )
}

@DevicePreviews
@Composable
private fun IgozogoTopAppBarPreview() {
    IgozogoTheme {
        IgozogoTopAppBar(
            title = stringResource(designR.string.core_design_place),
            scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        )
    }
}