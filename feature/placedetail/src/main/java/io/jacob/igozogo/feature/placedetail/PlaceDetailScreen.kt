package io.jacob.igozogo.feature.placedetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.jacob.igozogo.core.domain.model.Place

@Composable
fun PlaceDetailRoute(
    modifier: Modifier = Modifier,
    viewModel: PlaceDetailViewModel = hiltViewModel(),
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    val place = viewModel.place
    val state by viewModel.state.collectAsStateWithLifecycle()

//    when (val s = state) {
//        is PlaceDetailUiState.Loading -> PlaceDetailLoadingScreen(modifier)
//        is PlaceDetailUiState.Error -> PlaceDetailErrorScreen(modifier)
//        is PlaceDetailUiState.Success -> {
//            PlaceDetailScreen(
//                modifier = modifier,
//                place = place,
//                state = state,
//                onShowSnackbar = onShowSnackbar,
//            )
//        }
//    }
}

@Composable
fun PlaceDetailScreen(
    modifier: Modifier = Modifier,
    place: Place,
    state: PlaceDetailUiState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {

}