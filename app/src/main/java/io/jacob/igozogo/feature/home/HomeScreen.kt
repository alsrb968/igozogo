package io.jacob.igozogo.feature.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.jacob.igozogo.R
import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.feature.home.place.PlaceItemList
import io.jacob.igozogo.ui.shared.ChipItemList
import io.jacob.igozogo.ui.shared.TitleTextItem
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    val context = LocalContext.current
    val categoryPagingItems = viewModel.getPlaceCategories().collectAsLazyPagingItems()
    val placePagingItems = viewModel.getPlaces().collectAsLazyPagingItems()
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is HomeUiEffect.Synced -> onShowSnackbar(context.getString(R.string.place_sync_completed), "OK")
            }
        }
    }
    HomeScreen(
        modifier = modifier,
        categories = categoryPagingItems,
        places = placePagingItems
    )
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    categories: LazyPagingItems<String>,
    places: LazyPagingItems<Place>,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        state = rememberLazyListState()
    ) {
        item {
            TitleTextItem(text = stringResource(R.string.category)) {
                ChipItemList(
                    modifier = Modifier,
                    chipItems = categories,
                    onItemClick = { Timber.i(it) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            TitleTextItem(text = stringResource(R.string.place), onMore = { }) {
                PlaceItemList(
                    modifier = Modifier,
                    places = places,
                    isBookmarked = { false },
                    onBookmarkToggle = { },
                    onClick = { }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

    }
}