package io.jacob.igozogo.core.design.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

@Composable
internal fun thumbnailPlaceholderDefaultBrush(
    color: Color = MaterialTheme.colorScheme.surfaceVariant
): Brush {
    return SolidColor(color)
}