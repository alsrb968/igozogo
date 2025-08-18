package io.jacob.igozogo.core.model

data class SearchResult(
    val places: List<Place> = emptyList(),
    val stories: List<Story> = emptyList(),
)
