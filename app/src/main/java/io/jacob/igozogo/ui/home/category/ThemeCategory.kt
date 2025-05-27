package io.jacob.igozogo.ui.home.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Theaters
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import io.jacob.igozogo.core.domain.model.Theme
import io.jacob.igozogo.ui.theme.IgozogoTheme
import io.jacob.igozogo.ui.tooling.DevicePreviews

@Composable
fun ThemeCategoryItem(
    theme: Theme,
    placeholder: Painter = rememberVectorPainter(image = Icons.Default.Theaters),
    isBookmarked: Boolean,
    modifier: Modifier = Modifier,
    onToggleBookmarkClicked: () -> Unit,
) {

    Box(
        modifier = modifier
            .clickable { onToggleBookmarkClicked() },
    ) {
        AsyncImage(
            model = theme.imageUrl,
            contentDescription = theme.title,
            contentScale = ContentScale.Crop,
            placeholder = placeholder,
            error = rememberVectorPainter(image = Icons.Default.Error),
        )

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = theme.title,
            style = MaterialTheme.typography.labelLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@DevicePreviews
@Composable
private fun ThemeCategoryItemPreview() {
    IgozogoTheme {
        ThemeCategoryItem(

        )
    }
}