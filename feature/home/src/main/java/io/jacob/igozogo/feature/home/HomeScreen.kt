package io.jacob.igozogo.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.jacob.igozogo.core.design.R
import io.jacob.igozogo.core.design.component.ChipItemList
import io.jacob.igozogo.core.design.component.PlaceItemList
import io.jacob.igozogo.core.design.component.TitleTextItem
import io.jacob.igozogo.core.design.theme.IgozogoTheme
import io.jacob.igozogo.core.design.tooling.DevicePreviews
import io.jacob.igozogo.core.design.tooling.previewCategoryListLazyPagingItems
import io.jacob.igozogo.core.design.tooling.previewPlacesLazyPagingItems
import io.jacob.igozogo.core.domain.model.Place
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onPlaceClick: (Place) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    val context = LocalContext.current
    val categoryPagingItems = viewModel.getPlaceCategories().collectAsLazyPagingItems()
    val placePagingItems = viewModel.getPlaces().collectAsLazyPagingItems()
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is HomeUiEffect.Synced -> onShowSnackbar(context.getString(R.string.core_design_place_sync_completed), "OK")
            }
        }
    }
    HomeScreen(
        modifier = modifier,
        categories = categoryPagingItems,
        places = placePagingItems,
        onPlaceClick = onPlaceClick,
    )
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    categories: LazyPagingItems<String>,
    places: LazyPagingItems<Place>,
    onPlaceClick: (Place) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars),
        state = rememberLazyListState()
    ) {
        item {
            TitleTextItem(text = stringResource(R.string.core_design_category)) {
                ChipItemList(
                    modifier = Modifier,
                    chipItems = categories,
                    onItemClick = { Timber.Forest.i(it) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            TitleTextItem(text = stringResource(R.string.core_design_place), onMore = { }) {
                PlaceItemList(
                    modifier = Modifier,
                    places = places,
                    isBookmarked = { false },
                    onBookmarkToggle = { },
                    onClick = { place ->
                        onPlaceClick(place)
                    }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(300.dp))
        }

        item {
            Text(text = "End of list")
        }
    }
}

@DevicePreviews
@Composable
private fun HomeScreenPreview() {
    IgozogoTheme {
        HomeScreen(
            categories = previewCategoryListLazyPagingItems(),
            places = previewPlacesLazyPagingItems(),
            onPlaceClick = {}
        )
    }
}