package io.jacob.igozogo.feature.placedetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import io.jacob.igozogo.core.design.component.LoadingWheel
import io.jacob.igozogo.core.design.component.StoryItem
import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.core.domain.model.Story

@Composable
fun PlaceDetailRoute(
    modifier: Modifier = Modifier,
    viewModel: PlaceDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (val s = state) {
        is PlaceDetailUiState.Loading -> LoadingWheel(modifier = modifier)
        is PlaceDetailUiState.Error -> {}
        is PlaceDetailUiState.Success -> {
            PlaceDetailScreen(
                modifier = modifier,
                place = s.place,
                stories = s.stories.collectAsLazyPagingItems(),
                onBackClick = onBackClick,
                onShowSnackbar = onShowSnackbar
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceDetailScreen(
    modifier: Modifier = Modifier,
    place: Place,
    stories: LazyPagingItems<Story>,
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = place.title)
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackClick }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            LazyColumn(
                modifier = Modifier,
            ) {
                items(count = stories.itemCount) { index ->
                    stories[index]?.let { story ->
                        StoryItem(
                            story = story,
                            onItemClick = { /* TODO */ }
                        )
                    }
                }
            }
        }
    }
}