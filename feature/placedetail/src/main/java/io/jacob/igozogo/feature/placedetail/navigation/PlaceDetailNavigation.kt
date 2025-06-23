package io.jacob.igozogo.feature.placedetail.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.feature.placedetail.PlaceDetailRoute
import io.jacob.igozogo.feature.placedetail.PlaceDetailViewModel
import kotlinx.serialization.Serializable

@Serializable data class PlaceDetailRoute(val placeId: Int, val placeLangId: Int)

fun NavController.navigateToPlaceDetail(place: Place, navOptions: NavOptionsBuilder.() -> Unit = {}) =
    navigate(route = PlaceDetailRoute(place.placeId, place.placeLangId), navOptions)

fun NavGraphBuilder.placeDetailScreen(
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    composable<PlaceDetailRoute> { entry ->
        val placeId = entry.toRoute<PlaceDetailRoute>().placeId
        val placeLangId = entry.toRoute<PlaceDetailRoute>().placeLangId
        PlaceDetailRoute(
            viewModel = hiltViewModel<PlaceDetailViewModel, PlaceDetailViewModel.Factory>(
                key = "${placeId}:${placeLangId}"
            ) { factory ->
                factory.create(placeId, placeLangId)
            },
            onBackClick = onBackClick,
            onShowSnackbar = onShowSnackbar,
        )
    }
}