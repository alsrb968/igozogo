package io.jacob.igozogo.feature.placedetail.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.feature.placedetail.PlaceDetailRoute
import io.jacob.igozogo.feature.placedetail.PlaceDetailViewModel
import kotlinx.serialization.Serializable

@Serializable data class PlaceDetailRoute(val place: Place)

fun NavController.navigateToPlaceDetail(navOptions: NavOptions) =
    navigate(route = PlaceDetailRoute, navOptions)

fun NavGraphBuilder.placeDetailScreen(
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    composable<PlaceDetailRoute> { entry ->
        val place = entry.toRoute<PlaceDetailRoute>().place
        PlaceDetailRoute(
            viewModel = hiltViewModel<PlaceDetailViewModel, PlaceDetailViewModel.Factory>(
                key = place.placeLangId.toString()
            ) { factory ->
                factory.create(place)
            },
            onShowSnackbar = onShowSnackbar,
        )
    }
}