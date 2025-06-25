package io.jacob.igozogo.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.jacob.igozogo.core.domain.model.Place
import io.jacob.igozogo.core.domain.model.Story
import io.jacob.igozogo.core.domain.usecase.GetPlaceCategoriesUseCase
import io.jacob.igozogo.core.domain.usecase.GetPlacesUseCase
import io.jacob.igozogo.core.domain.usecase.GetStoriesByPlaceUseCase
import io.jacob.igozogo.core.domain.usecase.SyncPlacesUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val syncPlacesUseCase: SyncPlacesUseCase,
    private val getPlaceCategoriesUseCase: GetPlaceCategoriesUseCase,
    private val getPlacesUseCase: GetPlacesUseCase,
    private val getStoriesByPlaceUseCase: GetStoriesByPlaceUseCase,
) : ViewModel() {
    private val _state: MutableStateFlow<HomeState> = MutableStateFlow(HomeState.Loading)
    val state = _state.asStateFlow()

    private val _action = MutableSharedFlow<HomeAction>(extraBufferCapacity = 1)

    private val _effect = MutableSharedFlow<HomeEffect>(extraBufferCapacity = 1)
    val effect = _effect.asSharedFlow()

    init {
        handleActions()
        syncPlaces().invokeOnCompletion {
            loadFeedSections()
        }
    }

    private fun syncPlaces() = viewModelScope.launch {
        val isSynced = syncPlacesUseCase()
        if (isSynced) {
            _effect.emit(HomeEffect.Synced)
        }
    }

    private fun loadFeedSections() = viewModelScope.launch {
        _state.emit(
            HomeState.Success(
                listOf(
                    FeedSection.Categories(getPlaceCategoriesUseCase()),
                    FeedSection.Places(getPlacesUseCase()),
                    FeedSection.Stories(getStoriesByPlaceUseCase(testPlace))
                )
            )
        )
    }

    private fun handleActions() = viewModelScope.launch {
        _action.collectLatest { action ->
            when (action) {
                is HomeAction.ClickCategory -> clickCategory(action.category)
                is HomeAction.ClickPlace -> clickPlace(action.place)
                is HomeAction.ClickStory -> clickStory(action.story)
            }
        }
    }

    fun sendAction(action: HomeAction) = viewModelScope.launch {
        _action.emit(action)
    }

    private fun clickCategory(category: String) = viewModelScope.launch {
        _effect.emit(HomeEffect.NavigateToCategoryDetails(category))
    }

    private fun clickPlace(place: Place) = viewModelScope.launch {
        _effect.emit(HomeEffect.NavigateToPlaceDetails(place))
    }

    private fun clickStory(story: Story) = viewModelScope.launch {
        _effect.emit(HomeEffect.NavigateToStoryDetails(story))
    }

//    fun getPlaceCategories(): Flow<PagingData<String>> {
//        return getPlaceCategoriesUseCase().cachedIn(viewModelScope)
//    }
//
//    fun getPlaces(): Flow<PagingData<Place>> {
//        return getPlacesUseCase().cachedIn(viewModelScope)
//    }
}

sealed interface HomeState {
    data object Loading : HomeState
    data object Error : HomeState
    data class Success(val feedSections: List<FeedSection>) : HomeState
}

sealed interface HomeAction {
    data class ClickCategory(val category: String) : HomeAction
    data class ClickPlace(val place: Place) : HomeAction
    data class ClickStory(val story: Story) : HomeAction
}

sealed interface HomeEffect {
    data object Synced : HomeEffect
    data class NavigateToCategoryDetails(val category: String) : HomeEffect
    data class NavigateToPlaceDetails(val place: Place) : HomeEffect
    data class NavigateToStoryDetails(val story: Story) : HomeEffect
}

sealed interface FeedSection {
    data class Categories(val categories: List<String>) : FeedSection
    data class Places(val places: List<Place>) : FeedSection
    data class Stories(val stories: List<Story>) : FeedSection
}

private val testPlace = Place(
    placeId = 3375,
    placeLangId = 5093,
    placeCategory = "고궁 스토리텔링",
    address1 = "서울",
    address2 = "종로구",
    title = "경복궁",
    mapX = 126.97704,
    mapY = 37.579617,
    langCheck = "1000",
    langCode = "ko",
    imageUrl = "https://sfj608538-sfj608538.ktcdn.co.kr/file/sightImage/service/3375.jpg",
    createdTime = "20240125120046",
    modifiedTime = "20240125120100",
)