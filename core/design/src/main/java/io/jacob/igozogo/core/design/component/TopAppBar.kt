@file:OptIn(ExperimentalMaterial3Api::class)

package io.jacob.igozogo.core.design.component

import androidx.annotation.StringRes
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.jacob.igozogo.core.design.R
import io.jacob.igozogo.core.design.theme.IgozogoTheme
import io.jacob.igozogo.core.design.tooling.DevicePreviews

@Composable
fun IgozogoTopAppBar(
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(id = titleRes),
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
            titleRes = R.string.core_design_place,
        )
    }
}