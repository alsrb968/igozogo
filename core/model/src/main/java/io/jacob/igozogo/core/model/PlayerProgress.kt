package io.jacob.igozogo.core.model

data class PlayerProgress(
    val position: Long,
    val buffered: Long,
    val duration: Long,
) {
    val positionRatio: Float
        get() = position.toFloat() / duration

    val bufferedRatio: Float
        get() = buffered.toFloat() / duration
}
