package io.jacob.igozogo.ui.home.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.jacob.igozogo.core.domain.model.Theme
import io.jacob.igozogo.ui.shared.ThemeImage
import io.jacob.igozogo.ui.theme.IgozogoTheme
import io.jacob.igozogo.ui.tooling.DevicePreviews
import io.jacob.igozogo.ui.tooling.PreviewThemes

@Composable
fun ThemeCategoryList(
    modifier: Modifier = Modifier,
    themes: List<Theme>,
    isBookmarked: (Theme) -> Boolean,
    onBookmarkToggle: (Theme) -> Unit,
    onClick: (Theme) -> Unit,
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(themes.size) { index ->
            ThemeCategoryItem(
                theme = themes[index],
                isBookmarked = isBookmarked(themes[index]),
                onBookmarkToggle = { onBookmarkToggle(themes[index]) },
                onClick = { onClick(themes[index]) }
            )
        }
    }
}

@Composable
fun ThemeCategoryItem(
    modifier: Modifier = Modifier,
    theme: Theme,
    isBookmarked: Boolean,
    onBookmarkToggle: () -> Unit,
    onClick: () -> Unit,
) {
    val size = 160.dp

    Column(
        modifier = modifier
            .width(size)
            .clickable { onClick() },
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(RoundedCornerShape(30.dp))
        ) {

            ThemeImage(
                imageUrl = theme.imageUrl,
                contentDescription = theme.title,
                modifier = Modifier
                    .fillMaxSize()
            )

            IconButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp),
                onClick = { onBookmarkToggle() }
            ) {
                Icon(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = CircleShape,
                        ).padding(4.dp),
                    imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Bookmark",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = theme.title,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(12.dp),
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Location",
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "${theme.addr1} ${theme.addr2}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            }
        }
    }
}

@DevicePreviews
@Composable
private fun ThemeCategoryListPreview() {
    IgozogoTheme {
        ThemeCategoryList(
            themes = PreviewThemes,
            isBookmarked = { true },
            onBookmarkToggle = {},
            onClick = {}
        )
    }
}