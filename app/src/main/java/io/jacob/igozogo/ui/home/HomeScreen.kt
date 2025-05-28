package io.jacob.igozogo.ui.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import io.jacob.igozogo.ui.home.place.PlaceItemList
import io.jacob.igozogo.ui.shared.ChipItemList
import io.jacob.igozogo.ui.shared.TitleTextItem
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onSnackbar: (String) -> Unit,
) {
    val categoryPagingItems = viewModel.getPlaceCategories().collectAsLazyPagingItems()
    val placePagingItems = viewModel.getPlaces().collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is HomeUiEffect.Synced -> onSnackbar("Places synced")
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        state = rememberLazyListState()
    ) {
        item {
            TitleTextItem(text = "Category 카테고리")
        }

        item {
            ChipItemList(
                modifier = Modifier,
                chipItems = categoryPagingItems,
                onItemClick = { Timber.i(it) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            TitleTextItem(text = "Place 장소", onMore = {  })
        }

        item {
            PlaceItemList(
                modifier = Modifier,
                places = placePagingItems,
                isBookmarked = { false },
                onBookmarkToggle = {  },
                onClick = {  }
            )
        }
    }
}