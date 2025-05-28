package io.jacob.igozogo.ui.home.place

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.ui.shared.StateImage
import io.jacob.igozogo.ui.shared.ToggleFollowPodcastIconButton
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
    val padding = 8.dp

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = padding),
        horizontalArrangement = Arrangement.spacedBy(padding)
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

            StateImage(
                imageUrl = place.imageUrl,
                contentDescription = place.title,
                modifier = Modifier
                    .fillMaxSize()
            )

            ToggleFollowPodcastIconButton(
                modifier = Modifier
                    .align(Alignment.TopEnd),
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
                        text = "${place.addr1} ${place.addr2}",
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