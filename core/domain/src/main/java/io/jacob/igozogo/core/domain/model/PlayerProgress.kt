package io.jacob.igozogo.core.domain.model

data class PlayerProgress(
    val position: Long,
    val buffered: Long,
    val duration: Long,
    val bufferedPercentage: Int,
)
