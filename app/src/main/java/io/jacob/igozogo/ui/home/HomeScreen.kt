package io.jacob.igozogo.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
//    viewModel.syncThemes()

    val categoryPagingItems = viewModel.getThemeCategories().collectAsLazyPagingItems()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "Categories",
            style = MaterialTheme.typography.headlineLarge
        )

        LazyColumn(
            state = rememberLazyListState()
        ) {
            items(categoryPagingItems.itemCount) { index ->
                val category = categoryPagingItems[index]
                category?.isEmpty()?.let {
                    if (!it) {
                        Text(text = category)
                    }
                }
            }
        }
    }
}