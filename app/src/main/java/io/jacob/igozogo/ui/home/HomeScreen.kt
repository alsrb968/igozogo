package io.jacob.igozogo.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import io.jacob.igozogo.ui.shared.ChipItemList
import timber.log.Timber

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val categoryPagingItems = viewModel.getPlaceCategories().collectAsLazyPagingItems()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {

        LazyColumn(
            state = rememberLazyListState()
        ) {
            item {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Category",
                    style = MaterialTheme.typography.headlineLarge
                )
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
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Place",
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }
    }
}