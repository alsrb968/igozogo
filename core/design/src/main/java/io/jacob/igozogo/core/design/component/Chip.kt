package io.jacob.igozogo.core.design.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.jacob.igozogo.core.design.theme.IgozogoTheme
import io.jacob.igozogo.core.design.tooling.DevicePreviews
import io.jacob.igozogo.core.testing.data.categoryTestData

@Composable
fun ChipItemList(
    modifier: Modifier = Modifier,
    chipItems: List<String>,
    onItemClick: (String) -> Unit
) {
    val padding = 8.dp
    val chipHeight = 36.dp       // Chip 높이
    val chipSpacing = 8.dp       // 줄 사이 간격
    val rowCount = 4

    val totalHeight = chipHeight * rowCount + chipSpacing * (rowCount - 1) + padding * 2

    LazyHorizontalStaggeredGrid(
        modifier = modifier
            .height(totalHeight),
        state = rememberLazyStaggeredGridState(),
        rows = StaggeredGridCells.Fixed(rowCount),
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(chipSpacing),
        horizontalItemSpacing = chipSpacing,
    ) {
        items(
            count = chipItems.size,
            key = { index -> chipItems[index] }
        ) { index ->
            chipItems[index].let { item ->
                if (item.isEmpty()) {
                    return@items
                }

                FilterChip(
                    selected = true,
                    modifier = Modifier
                        .height(chipHeight),
                    label = {
                        Text(text = item)
                    },
                    shape = CircleShape,
                    onClick = { onItemClick(item) }
                )
            }
        }
    }
}

@DevicePreviews
@Composable
private fun ChipItemListPreview() {
    IgozogoTheme {
        ChipItemList(
            chipItems = categoryTestData,
            onItemClick = {}
        )
    }
}