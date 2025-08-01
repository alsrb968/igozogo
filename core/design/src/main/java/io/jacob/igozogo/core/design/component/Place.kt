package io.jacob.igozogo.core.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.jacob.igozogo.core.design.icon.IgozogoIcons
import io.jacob.igozogo.core.design.theme.IgozogoTheme
import io.jacob.igozogo.core.design.tooling.DevicePreviews
import io.jacob.igozogo.core.design.tooling.PreviewPlace
import io.jacob.igozogo.core.domain.model.Place

@Composable
fun PlaceItemList(
    modifier: Modifier = Modifier,
    places: List<Place>,
    isBookmarked: (Place) -> Boolean,
    onBookmarkToggle: (Place) -> Unit,
    onItemClick: (Place) -> Unit,
) {
    val padding = 16.dp

    LazyRow(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(padding),
        contentPadding = PaddingValues(horizontal = padding)
    ) {
        items(
            count = places.size,
            key = { places[it].placeLangId },
        ) { index ->
            places[index].let { place ->
                PlaceItem(
                    place = place,
                    isBookmarked = isBookmarked(place),
                    onBookmarkToggle = { onBookmarkToggle(place) },
                    onClick = { onItemClick(place) }
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
                                MaterialTheme.colorScheme.onPrimary,
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
                    color = MaterialTheme.colorScheme.primary,
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
                        imageVector = IgozogoIcons.Place,
                        contentDescription = "Location",
                        tint = MaterialTheme.colorScheme.primary,
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = place.fullAddress,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
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