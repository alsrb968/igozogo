package io.jacob.igozogo.feature.home.place

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.ui.shared.StateImage
import io.jacob.igozogo.ui.shared.ToggleFollowIconButton
import io.jacob.igozogo.ui.theme.IgozogoTheme
import io.jacob.igozogo.ui.tooling.DevicePreviews
import io.jacob.igozogo.ui.tooling.PreviewPlace

@Composable
fun PlaceItemList(
    modifier: Modifier = Modifier,
    places: LazyPagingItems<Place>,
    isBookmarked: (Place) -> Boolean,
    onBookmarkToggle: (Place) -> Unit,
    onClick: (Place) -> Unit,
) {
    val padding = 16.dp

    LazyRow(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(padding),
        contentPadding = PaddingValues(horizontal = padding)
    ) {
        items(places.itemCount) { index ->
            places[index]?.let { place ->
                PlaceItem(
                    place = place,
                    isBookmarked = isBookmarked(place),
                    onBookmarkToggle = { onBookmarkToggle(place) },
                    onClick = { onClick(place) }
                )
            }
        }
    }
}

@Composable
fun PlaceItem(
    modifier: Modifier = Modifier,
    place: Place,
    isBookmarked: Boolean,
    onBookmarkToggle: () -> Unit,
    onClick: () -> Unit,
) {
    val size = 220.dp

    Card(
        modifier = modifier
            .width(size)
            .height(size * 1.5f),
        shape = RoundedCornerShape(30.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = onClick,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            StateImage(
                modifier = Modifier
                    .fillMaxSize(),
                imageUrl = place.imageUrl,
                contentDescription = place.title,
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .height(size * 0.6f)
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        )
                    )
            )

            ToggleFollowIconButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                isFollowed = isBookmarked,
                onClick = { onBookmarkToggle() }
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = place.title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
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
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Default.Place,
                        contentDescription = "Location",
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "${place.addr1} ${place.addr2}",
                        style = MaterialTheme.typography.labelLarge,
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
private fun PlaceItemPreview() {
    IgozogoTheme {
        PlaceItem(
            place = PreviewPlace,
            isBookmarked = false,
            onBookmarkToggle = {},
            onClick = {}
        )
    }
}