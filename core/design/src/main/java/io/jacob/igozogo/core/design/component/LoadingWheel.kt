package io.jacob.igozogo.core.design.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.jacob.igozogo.core.design.theme.IgozogoTheme
import io.jacob.igozogo.core.design.tooling.DevicePreviews

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoadingWheel(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        ContainedLoadingIndicator(
            modifier = Modifier
                .align(Alignment.Center),
        )
    }
}

@DevicePreviews
@Composable
private fun LoadingWheelPreview() {
    IgozogoTheme {
        LoadingWheel()
    }
}